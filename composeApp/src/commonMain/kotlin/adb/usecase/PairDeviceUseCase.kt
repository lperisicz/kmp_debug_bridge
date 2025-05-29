package adb.usecase

import adb.PairingSession
import externalcommand.runSuspending
import logger.Logger

internal interface PairDeviceUseCase {

    suspend operator fun invoke(pairInfo: String)
}

internal class PairDeviceUseCaseImpl(
    private val logger: Logger,
) : PairDeviceUseCase {

    override suspend fun invoke(pairInfo: String) {
        logger.logI("Pairing to $pairInfo ${PairingSession.passcode}")
        ProcessBuilder("adb", "pair", pairInfo, PairingSession.passcode)
            .redirectErrorStream(true)
            .runSuspending()
    }
}
