package di

import adb.usecase.ConnectDeviceUseCase
import adb.usecase.ConnectDeviceUseCaseImpl
import adb.usecase.GenerateWifiPairingInfoUseCase
import adb.usecase.GenerateWifiPairingInfoUseCaseImpl
import adb.usecase.GetAvailableDevicesUseCase
import adb.usecase.GetAvailableDevicesUseCaseImpl
import adb.usecase.GetMdnsServicesUseCase
import adb.usecase.GetMdnsServicesUseCaseImpl
import adb.usecase.GetProcessIdUseCase
import adb.usecase.GetProcessIdUseCaseImpl
import adb.usecase.PairDeviceUseCase
import adb.usecase.PairDeviceUseCaseImpl
import adb.usecase.RecordDevicePropsUseCase
import adb.usecase.RecordDevicePropsUseCaseImpl
import adb.usecase.RecordDumpSysUseCase
import adb.usecase.RecordDumpSysUseCaseImpl
import adb.usecase.RecordProcessLogsUseCase
import adb.usecase.RecordProcessLogsUseCaseImpl
import adb.usecase.StartAppUseCase
import adb.usecase.StartAppUseCaseImpl
import device.DevicePickerViewModel
import device.DevicePickerViewModelImpl
import di.qualifiers.CoroutineScopeQualifier
import home.HomeViewModel
import home.HomeViewModelImpl
import install.InstallUseCase
import install.InstallUseCaseImpl
import install.InstallViewModel
import install.InstallViewModelImpl
import io.usecase.CreateSessionDirectoryUseCase
import io.usecase.CreateSessionDirectoryUseCaseImpl
import io.usecase.PackageSessionDirectoryUseCase
import io.usecase.PackageSessionDirectoryUseCaseImpl
import logger.ClearLogOutputUseCase
import logger.ClearLogOutputUseCaseImpl
import logger.GetLogOutputUseCase
import logger.GetLogOutputUseCaseImpl
import logger.Logger
import logger.LoggerImpl
import logger.LoggerStream
import logger.LoggerViewModel
import logger.LoggerViewModelImpl
import notification.NotificationsViewModel
import notification.NotificationsViewModelImpl
import notification.PublishNotificationUseCase
import notification.PublishNotificationUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import qr.QRPairViewModel
import qr.QRPairViewModelImpl
import record.GetRecordingSessionUseCase
import record.GetRecordingSessionUseCaseImpl
import record.RecordViewModel
import record.RecordViewModelImpl
import record.RecordingSessionSource
import record.RecordingSessionSourceImpl
import record.RestartRecordingSessionUseCase
import record.RestartRecordingSessionUseCaseImpl
import record.StartRecordingUseCase
import record.StartRecordingUseCaseImpl
import scrcpy.usecase.RecordScreenUseCase
import scrcpy.usecase.RecordScreenUseCaseImpl
import settings.SetSessionDeviceUseCase
import settings.SetSessionDeviceUseCaseImpl
import settings.SetSessionPackageNameUseCase
import settings.SetSessionPackageNameUseCaseImpl
import settings.SetSessionWorkspaceUseCase
import settings.SetSessionWorkspaceUseCaseImpl
import settings.SettingsViewModel
import settings.SettingsViewModelImpl
import settings.UpdateSelectedDeviceUseCase
import settings.UpdateSelectedDeviceUseCaseImpl

internal val recordingModule = module {

    factory<RecordViewModel> {
        RecordViewModelImpl(
            logger = get(),
            startRecordingUseCase = get(),
            packageSessionDirectoryUseCase = get(),
        )
    }

    factory<SettingsViewModel> {
        SettingsViewModelImpl(
            getRecordingSessionUseCase = get(),
            setSessionWorkspaceUseCase = get(),
            setSessionPackageNameUseCase = get(),
            logger = get(),
        )
    }

    factory<HomeViewModel> {
        HomeViewModelImpl()
    }

    single<RecordingSessionSource> {
        RecordingSessionSourceImpl()
    }

    single<GetRecordingSessionUseCase> {
        GetRecordingSessionUseCaseImpl(
            recordingSessionSource = get()
        )
    }

    single<RestartRecordingSessionUseCase> {
        RestartRecordingSessionUseCaseImpl(
            recordingSessionSource = get(),
        )
    }

    single<SetSessionWorkspaceUseCase> {
        SetSessionWorkspaceUseCaseImpl(
            recordingSessionSource = get(),
        )
    }

    single<SetSessionPackageNameUseCase> {
        SetSessionPackageNameUseCaseImpl(
            sessionSource = get(),
        )
    }

    single<SetSessionDeviceUseCase> {
        SetSessionDeviceUseCaseImpl(
            sessionSource = get(),
        )
    }

    single<StartRecordingUseCase> {
        StartRecordingUseCaseImpl(
            logger = get(),
            restartRecordingSessionUseCase = get(),
            createSessionDirectoryUseCase = get(),
            recordProcessLogsUseCase = get(),
            recordDevicePropsUseCase = get(),
            recordDumpSysUseCase = get(),
            recordScreenUseCase = get(),
            getRecordingSessionUseCase = get(),
        )
    }

    single<RecordDevicePropsUseCase> {
        RecordDevicePropsUseCaseImpl(
            getRecordingSessionUseCase = get(),
            logger = get(),
        )
    }

    single<RecordProcessLogsUseCase> {
        RecordProcessLogsUseCaseImpl(
            logger = get(),
            getRecordingSessionUseCase = get(),
            getProcessIdUseCase = get(),
        )
    }

    single<RecordDumpSysUseCase> {
        RecordDumpSysUseCaseImpl(
            getRecordingSessionUseCase = get(),
            logger = get(),
        )
    }

    single<RecordScreenUseCase> {
        RecordScreenUseCaseImpl(
            getRecordingSessionUseCase = get(),
        )
    }

    single<GetAvailableDevicesUseCase> {
        GetAvailableDevicesUseCaseImpl()
    }

    single { LoggerImpl() }

    single<Logger> { get<LoggerImpl>() }

    single<LoggerStream> { get<LoggerImpl>() }

    single<GetLogOutputUseCase> {
        GetLogOutputUseCaseImpl(
            loggerStream = get(),
        )
    }

    single<ClearLogOutputUseCase> {
        ClearLogOutputUseCaseImpl(
            loggerStream = get(),
        )
    }

    factory<LoggerViewModel> {
        LoggerViewModelImpl(
            getLogOutputUseCase = get(),
            clearLogOutputUseCase = get(),
        )
    }

    single<GetProcessIdUseCase> {
        GetProcessIdUseCaseImpl(
            getRecordingSessionUseCase = get(),
        )
    }

    single<UpdateSelectedDeviceUseCase> {
        UpdateSelectedDeviceUseCaseImpl(
            getAvailableDevicesUseCase = get(),
            getRecordingSessionUseCase = get(),
            setSessionDeviceUseCase = get(),
        )
    }

    factory<DevicePickerViewModel> {
        DevicePickerViewModelImpl(
            getAvailableDevicesUseCase = get(),
            getRecordingSessionUseCase = get(),
            setSessionDeviceUseCase = get(),
            updateSelectedDeviceUseCase = get(),
            startAppUseCase = get(),
        )
    }

    factory<InstallViewModel> {
        InstallViewModelImpl(
            logger = get(),
            getRecordingSessionUseCase = get(),
            installUseCase = get(),
            startAppUseCase = get(),
        )
    }

    single<InstallUseCase> {
        InstallUseCaseImpl(
            logger = get(),
            getRecordingSessionUseCase = get(),
        )
    }

    single<StartAppUseCase> {
        StartAppUseCaseImpl(
            getRecordingSessionUseCase = get(),
            logger = get(),
        )
    }

    single<GenerateWifiPairingInfoUseCase> {
        GenerateWifiPairingInfoUseCaseImpl(
            logger = get(),
        )
    }

    single<GetMdnsServicesUseCase> {
        GetMdnsServicesUseCaseImpl(
            logger = get(),
        )
    }

    factory<QRPairViewModel> {
        QRPairViewModelImpl(
            generateWifiPairingInfoUseCase = get(),
            getMdnsServicesUseCase = get(),
            updateSelectedDeviceUseCase = get(),
            pairDeviceUseCase = get(),
            connectDeviceUseCase = get(),
        )
    }

    single<PairDeviceUseCase> {
        PairDeviceUseCaseImpl(
            logger = get(),
        )
    }

    single<ConnectDeviceUseCase> {
        ConnectDeviceUseCaseImpl(
            logger = get(),
        )
    }

    single<CreateSessionDirectoryUseCase> {
        CreateSessionDirectoryUseCaseImpl(
            logger = get(),
            getRecordingSessionUseCase = get(),
        )
    }

    single<PackageSessionDirectoryUseCase> {
        PackageSessionDirectoryUseCaseImpl(
            logger = get(),
            getRecordingSessionUseCase = get(),
            publishNotificationUseCase = get(),
        )
    }

    factory<NotificationsViewModel> {
        NotificationsViewModelImpl()
    }

    single<PublishNotificationUseCase> {
        PublishNotificationUseCaseImpl(
            logger = get(),
            appScope = get(qualifier = named(CoroutineScopeQualifier.App))
        )
    }
}
