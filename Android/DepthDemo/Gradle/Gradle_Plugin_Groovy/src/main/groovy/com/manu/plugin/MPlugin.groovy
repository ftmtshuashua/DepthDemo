package com.manu.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 自定义插件
 */
class MPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.task('taskPlugin') {
            doLast {
                println "自定义插件创建任务"
            }
        }
    }
}

