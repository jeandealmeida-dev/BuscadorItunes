import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

/**
 * Apply configuration settings that are shared across all modules.
 */
fun PluginContainer.applyBaseConfig(project: Project) {
    whenPluginAdded {
        when (this) {
//            is com.android.build.gradle.internal.plugins.AppPlugin -> {
//                project.extensions
//                    .getByType<AppExtension>()
//                    .apply {
//                        baseConfig()
//                    }
//            }
//            is LibraryPlugin -> {
//                project.extensions
//                    .getByType<LibraryExtension>()
//                    .apply {
//                        baseConfig()
//                    }
//            }
        }
    }
}