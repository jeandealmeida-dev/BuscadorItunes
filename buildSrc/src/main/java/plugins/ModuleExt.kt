package plugins

import org.gradle.api.Action

open class MyModuleExtension {
    open val jacoco: JacocoOptions = JacocoOptions()
    open fun jacoco(action: Action<JacocoOptions>) {
        action.execute(jacoco)
    }
}

open class JacocoOptions {
    open var isEnabled: Boolean = true

    open var excludes: ArrayList<String> = arrayListOf(
        "**/di/**"
    )

    private fun excludes(vararg excludes: String) {
        this.excludes.addAll(excludes)
    }

    open fun excludesUI() {
        excludes(
            "**/*Holder.class",
            "**/*Activity.class",
            "**/*Fragment.class",
            "**/*Adapter.class",
            "**/*BottomSheet.class",
        )
    }
}