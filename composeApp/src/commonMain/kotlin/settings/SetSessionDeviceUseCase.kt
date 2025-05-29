package settings

import adb.ConnectedDevice
import record.RecordingSessionSource

internal interface SetSessionDeviceUseCase {

    operator fun invoke(device: ConnectedDevice?)
}

internal class SetSessionDeviceUseCaseImpl(
    private val sessionSource: RecordingSessionSource,
): SetSessionDeviceUseCase {

    override fun invoke(device: ConnectedDevice?) = sessionSource.setDevice(device)
}
