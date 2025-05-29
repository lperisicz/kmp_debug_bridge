package record

import adb.ConnectedDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import persistance.PreferencesSource
import java.time.Instant

internal interface RecordingSessionSource {

    fun restartTimestamp()

    fun setDevice(device: ConnectedDevice?)

    fun setPackageName(packageName: String)

    fun setWorkspace(workspace: String)

    fun getSession(): Flow<RecordingSession>
}

private const val PACKAGE_NAME_KEY = "packageName"
private const val PACKAGE_WORKSPACE_KEY = "workspace"

internal class RecordingSessionSourceImpl : RecordingSessionSource {

    private val state by lazy {
        val session = RecordingSession(
            packageName = PreferencesSource.getString(PACKAGE_NAME_KEY, ""),
            workspace = PreferencesSource.getString(PACKAGE_WORKSPACE_KEY, ""),
        )
        MutableStateFlow(value = session)
    }

    override fun restartTimestamp() = state.update { it.copy(timestamp = Instant.now().toEpochMilli()) }

    override fun setDevice(device: ConnectedDevice?) = state.update { it.copy(device = device) }

    override fun setPackageName(packageName: String) {
        PreferencesSource.putString(PACKAGE_NAME_KEY, packageName)
        state.update { it.copy(packageName = packageName) }
    }

    override fun setWorkspace(workspace: String) {
        PreferencesSource.putString(PACKAGE_WORKSPACE_KEY, workspace)
        state.update { it.copy(workspace = workspace) }
    }

    override fun getSession(): Flow<RecordingSession> = state
}

internal data class RecordingSession(
    val device: ConnectedDevice? = null,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val packageName: String,
    val workspace: String,
)
