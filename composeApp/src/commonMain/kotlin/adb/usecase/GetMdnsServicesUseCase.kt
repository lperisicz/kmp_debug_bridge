package adb.usecase

import adb.MdnsServices
import adb.PairingSession
import adb.PairingState
import externalcommand.use
import kotlinx.coroutines.delay
import logger.Logger
import java.io.BufferedReader
import java.io.InputStreamReader

internal interface GetMdnsServicesUseCase {

    suspend operator fun invoke(): MdnsServices
}

private const val POOLING_RETRY_DELAY = 1_000L
private const val PAIR_SERVICE_INFO = "_adb-tls-pairing._tcp."
private const val CONNECT_SERVICE_INFO = "_adb-tls-connect._tcp."

internal class GetMdnsServicesUseCaseImpl(
    private val logger: Logger,
) : GetMdnsServicesUseCase {

    private val ipAddressRegex = Regex("""(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})""")
    private val infoRegex = Regex("""(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}:\d{1,5})""")
    private val idRegex = Regex("""^(.*?)\s+_adb-tls-connect""")

    override suspend fun invoke(): MdnsServices {
        logger.logI("Started pooling mdns services.")
        PairingSession.updateState(PairingState.PAIRING)
        while (PairingSession.state() == PairingState.PAIRING) {
            val services = getServices()
            if (services != null) {
                logger.logI("Services found.")
                return services
            }
            delay(POOLING_RETRY_DELAY)
        }
        logger.logI("Pooling stopped, session in wrong state.")
        throw RuntimeException("Pairing services not found.")
    }

    private suspend fun getServices(): MdnsServices? =
        executeCommand()
            .groupBy { ipAddressRegex.find(it)?.groupValues?.getOrNull(1) }
            .map { (_, lines) -> lines.getServices() }
            .filterNotNull()
            .firstOrNull()

    private fun List<String>.getServices(): MdnsServices? {
        val pairingInfo = firstOrNull { info ->
            info.contains(PairingSession.device) && info.contains(PAIR_SERVICE_INFO)
        }?.let(infoRegex::find)?.groupValues?.getOrNull(1)
        if (pairingInfo == null) {
            return null
        }
        val connectInfo = reversed()
            .firstOrNull { info ->
                info.contains(CONNECT_SERVICE_INFO)
            }?.let(idRegex::find)?.groupValues?.getOrNull(1)
        if (connectInfo == null) {
            return null
        }
        return MdnsServices(
            pairingInfo = pairingInfo,
            connectingInfo = connectInfo,
        )
    }
}

private suspend fun executeCommand(): List<String> =
    ProcessBuilder("adb", "mdns", "services")
        .redirectErrorStream(true)
        .use { process ->
            BufferedReader(
                InputStreamReader(
                    process.inputStream
                )
            ).readText()
        }
        .lines()
        .drop(1)
