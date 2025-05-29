package adb.usecase

import adb.PairingSession
import logger.Logger

internal interface GenerateWifiPairingInfoUseCase {

    operator fun invoke(): String
}

private const val FORMAT_QR = "WIFI:T:ADB;S:%s;P:%s;;"

internal class GenerateWifiPairingInfoUseCaseImpl(
    private val logger: Logger,
) : GenerateWifiPairingInfoUseCase {

    override fun invoke(): String {
        logger.logI("Generating wifi pairing info.")
        PairingSession.initialise()
        return generateInfo()
    }

    private fun generateInfo(): String = FORMAT_QR.format(PairingSession.device, PairingSession.passcode)
}
