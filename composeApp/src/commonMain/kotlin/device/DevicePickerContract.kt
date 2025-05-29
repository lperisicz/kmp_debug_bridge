package device

import adb.ConnectedDevice
import adb.usecase.GetAvailableDevicesUseCase
import adb.usecase.StartAppUseCase
import core.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import record.GetRecordingSessionUseCase
import record.RecordingSession
import settings.SetSessionDeviceUseCase
import settings.UpdateSelectedDeviceUseCase

internal sealed class DevicePickerViewState {

    data class Picker(
        val selected: ConnectedDevice?,
        val devices: List<ConnectedDevice>,
    ) : DevicePickerViewState() {

        companion object {
            val initial = Picker(
                selected = null,
                devices = emptyList()
            )
        }
    }
}

internal abstract class DevicePickerViewModel : BaseViewModel<DevicePickerViewState>() {

    abstract fun selectDevice(device: ConnectedDevice)

    abstract fun refreshDevices()

    abstract fun run()
}

internal class DevicePickerViewModelImpl(
    private val getAvailableDevicesUseCase: GetAvailableDevicesUseCase,
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
    private val setSessionDeviceUseCase: SetSessionDeviceUseCase,
    private val updateSelectedDeviceUseCase: UpdateSelectedDeviceUseCase,
    private val startAppUseCase: StartAppUseCase,
) : DevicePickerViewModel() {

    private val availableDevices = MutableStateFlow<List<ConnectedDevice>>(emptyList())

    init {
        runCommand {
            refreshAvailableDevices()
        }

        query {
            combine(
                getRecordingSessionUseCase(),
                availableDevices.filterNotNull(),
                ::mapToPickerState,
            )
        }
    }

    override fun selectDevice(device: ConnectedDevice) = setSessionDeviceUseCase(device)

    override fun refreshDevices() {
        runCommand { refreshAvailableDevices() }
    }

    private suspend fun refreshAvailableDevices() {
        updateSelectedDeviceUseCase()
        availableDevices.update { getAvailableDevicesUseCase() }
    }

    override fun run() {
        runCommand { startAppUseCase() }
    }
}

private fun mapToPickerState(recordingSession: RecordingSession, availableDevices: List<ConnectedDevice>): DevicePickerViewState.Picker =
    DevicePickerViewState.Picker(
        selected = recordingSession.device,
        devices = availableDevices,
    )
