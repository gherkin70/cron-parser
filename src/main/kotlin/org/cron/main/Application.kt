package org.cron.main

import org.cron.CronParser

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please input a cron string")
    } else {
        println(CronParser().parseCron(args[0]))
    }
}