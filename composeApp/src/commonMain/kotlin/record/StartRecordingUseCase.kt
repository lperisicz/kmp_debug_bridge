package record

import adb.usecase.RecordDevicePropsUseCase
import adb.usecase.RecordDumpSysUseCase
import adb.usecase.RecordProcessLogsUseCase
import io.usecase.CreateSessionDirectoryUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import logger.Logger
import scrcpy.usecase.RecordScreenUseCase

internal interface StartRecordingUseCase {

    suspend operator fun invoke()
}

internal class StartRecordingUseCaseImpl(
    private val logger: Logger,
    private val restartRecordingSessionUseCase: RestartRecordingSessionUseCase,
    private val createSessionDirectoryUseCase: CreateSessionDirectoryUseCase,
    private val recordDevicePropsUseCase: RecordDevicePropsUseCase,
    private val recordProcessLogsUseCase: RecordProcessLogsUseCase,
    private val recordDumpSysUseCase: RecordDumpSysUseCase,
    private val recordScreenUseCase: RecordScreenUseCase,
    private val getRecordingSessionUseCase: GetRecordingSessionUseCase,
) : StartRecordingUseCase {

    override suspend fun invoke() {
        coroutineScope {
            restartRecordingSessionUseCase()

            val session = getRecordingSessionUseCase().first()
            logger.logI("Started recording session: $session")

            if (session.device == null) {
                logger.logE("Device not available")
                return@coroutineScope
            }

            createSessionDirectoryUseCase()

            val recordLogsJob = launch { recordProcessLogsUseCase() }
            val recordDevicePropsJob = launch { recordDevicePropsUseCase() }
            val recordDumpSysJob = launch { recordDumpSysUseCase() }
            val recordScreenJob = launch { recordScreenUseCase() }

            joinAll(
                recordLogsJob,
                recordDevicePropsJob,
                recordDumpSysJob,
                recordScreenJob,
            )
        }
    }
}
