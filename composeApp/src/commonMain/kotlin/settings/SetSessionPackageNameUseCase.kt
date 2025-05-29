package settings

import record.RecordingSessionSource

internal interface SetSessionPackageNameUseCase {

    operator fun invoke(packageName: String)
}

internal class SetSessionPackageNameUseCaseImpl(
    private val sessionSource: RecordingSessionSource,
) : SetSessionPackageNameUseCase {

    override fun invoke(packageName: String) = sessionSource.setPackageName(packageName)
}
