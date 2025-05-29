package adb.usecase

import externalcommand.runSuspending
import logger.Logger

internal interface ConnectDeviceUseCase {

    suspend operator fun invoke(connectInfo: String)
}

internal class ConnectDeviceUseCaseImpl(
    private val logger: Logger,
) : ConnectDeviceUseCase {

    override suspend fun invoke(connectInfo: String) {
        logger.logI("Connecting to $connectInfo")
        ProcessBuilder("adb", "connect", connectInfo)
            .redirectErrorStream(true)
            .runSuspending()

        // device already paired to, disconnecting using same
        // info so both connections are not present as unique device
        ProcessBuilder("adb", "disconnect", connectInfo)
            .redirectErrorStream(true)
            .runSuspending()
    }
}
