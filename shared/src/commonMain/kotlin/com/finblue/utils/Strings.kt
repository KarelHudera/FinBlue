package com.finblue.utils

import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import com.finblue.MR

fun getNetworkFailed() = StringDesc.Resource(MR.strings.network_failed).resolve()

fun getRequestFailed() = StringDesc.Resource(MR.strings.request_failed).resolve()

fun getServerFailed() = StringDesc.Resource(MR.strings.sever_failed).resolve()

fun getUnexpectedError() = StringDesc.Resource(MR.strings.unexpected_error).resolve()

fun getMoreItemText() = StringDesc.Resource(MR.strings.more_item).resolve()

fun getRetryText() = StringDesc.Resource(MR.strings.retry).resolve()