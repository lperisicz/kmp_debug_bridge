package qr

import adb.usecase.ConnectDeviceUseCase
import adb.usecase.GenerateWifiPairingInfoUseCase
import adb.usecase.GetMdnsServicesUseCase
import adb.usecase.PairDeviceUseCase
import core.viewModel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import settings.UpdateSelectedDeviceUseCase

internal sealed class QRPairViewState {

    data class Pairing(val pairInfo: String) : QRPairViewState() {

        companion object {
            val initial = Pairing("")
        }
    }

    data class Connecting(val visible: Boolean) : QRPairViewState() {

        companion object {
            val initial = Connecting(false)
        }
    }

    data class Closing(val closing: Boolean) : QRPairViewState() {

        companion object {
            val initial = Closing(false)
        }
    }
}

internal abstract class QRPairViewModel : BaseViewModel<QRPairViewState>()

internal class QRPairViewModelImpl(
    private val generateWifiPairingInfoUseCase: GenerateWifiPairingInfoUseCase,
    private val getMdnsServicesUseCase: GetMdnsServicesUseCase,
    private val updateSelectedDeviceUseCase: UpdateSelectedDeviceUseCase,
    private val pairDeviceUseCase: PairDeviceUseCase,
    private val connectDeviceUseCase: ConnectDeviceUseCase,
) : QRPairViewModel() {

    private val pairingInfo = MutableStateFlow<String?>(null)
    private val connectingState = MutableStateFlow(false)
    private val closingState = MutableStateFlow(false)

    init {
        println("trigger QRPairViewModelImpl.init")

        runCommand {
            startPairingProcess()
        }

        query {
            pairingInfo
                .filterNotNull()
                .map(QRPairViewState::Pairing)
        }

        query {
            connectingState
                .map(QRPairViewState::Connecting)
        }

        query {
            closingState
                .map(QRPairViewState::Closing)
        }
    }

    override fun close() {
        super.close()
        println("trigger QRPairViewModelImpl.close")
    }

    private suspend fun startPairingProcess() {
        // show qr code
        val info = generateWifiPairingInfoUseCase()
        pairingInfo.update { info }

        // await device mdns services
        val services = getMdnsServicesUseCase()

        // pairing and connecting
        connectingState.update { true }
        pairDeviceUseCase(services.pairingInfo)
        connectDeviceUseCase(services.connectingInfo)

        // updating selected device if needed
        updateSelectedDeviceUseCase()

        // hide transient
        closingState.update { true }
    }
}
