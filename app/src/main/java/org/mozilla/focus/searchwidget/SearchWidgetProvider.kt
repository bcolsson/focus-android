/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus.searchwidget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import mozilla.components.feature.search.widget.AppSearchWidgetProvider
import mozilla.components.feature.search.widget.BaseVoiceSearchActivity
import mozilla.components.feature.search.widget.SearchWidgetConfig
import mozilla.components.support.utils.PendingIntentUtils
import org.mozilla.focus.R
import org.mozilla.focus.activity.IntentReceiverActivity
import org.mozilla.focus.ext.components

class SearchWidgetProvider : AppSearchWidgetProvider() {

    override fun onEnabled(context: Context) {
        context.components.settings.addSearchWidgetInstalled(1)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        context.components.settings.addSearchWidgetInstalled(-appWidgetIds.size)
    }

    override val config: SearchWidgetConfig =
        SearchWidgetConfig(
            searchWidgetIconResource = R.drawable.ic_splash_screen,
            searchWidgetMicrophoneResource = R.drawable.mozac_ic_microphone,
            appName = R.string.app_name
        )

    override fun createTextSearchIntent(context: Context): PendingIntent {
        val textSearchIntent = Intent(context, IntentReceiverActivity::class.java)
            .apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                this.putExtra(IntentReceiverActivity.SEARCH_WIDGET_EXTRA, true)
            }
        return PendingIntent.getActivity(
            context,
            REQUEST_CODE_NEW_TAB,
            textSearchIntent,
            PendingIntentUtils.defaultFlags or
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun shouldShowVoiceSearch(context: Context): Boolean {
        return true
    }

    override fun voiceSearchActivity(): Class<out BaseVoiceSearchActivity> {
        return VoiceSearchActivity::class.java
    }

    companion object {
        @VisibleForTesting
        const val REQUEST_CODE_NEW_TAB = 0
    }
}
