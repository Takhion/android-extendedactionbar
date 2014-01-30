package me.eugeniomarletti.extendedactionbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Example {@code Activity} to show how to automatically eliminate overdraw caused by the window background.
 * <p>
 * Uses the attributes {@link me.eugeniomarletti.extendedactionbar.R.drawable#window_background} and
 * {@link me.eugeniomarletti.extendedactionbar.R.drawable#statusbar_background} (if supported) to automatically apply
 * the correct backgrounds.
 * @see
 * </p>
 */
public class ExtendedActionBarActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final Resources res = getResources();

        // replace window background to reduce overdraw
        final ViewGroup contentView = (ViewGroup)findViewById(android.R.id.content);
        final View content = contentView.getChildAt(0);
        final Drawable windowBackground = res.getDrawable(R.drawable.window_background);
        if (windowBackground == null)
            throw new IllegalStateException("The attribute R.drawable.window_background must be set.");
        getWindow().setBackgroundDrawable(null);
        setBackground(content, windowBackground);

        // add statusbar background
        if (Build.VERSION.SDK_INT >= 19)
        {
            // check if translucent bars are enabled
            final int config_enableTranslucentDecor_id =
                    res.getIdentifier("config_enableTranslucentDecor", "bool", "android");
            if (config_enableTranslucentDecor_id > 0 && res.getBoolean(config_enableTranslucentDecor_id))
            {
                // get ActionBar container
                final View actionBarContainer = findViewById("action_bar_container", "android");
                if (actionBarContainer != null)
                {
                    // add layout listener (can't get margin before layout)
                    //noinspection ConstantConditions
                    actionBarContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                            .OnGlobalLayoutListener()
                    {
                        @SuppressWarnings("ConstantConditions")
                        @Override
                        public void onGlobalLayout()
                        {
                            // remove layout listener
                            final ViewTreeObserver vto = actionBarContainer.getViewTreeObserver();
                            if (Build.VERSION.SDK_INT < 16)
                                vto.removeGlobalOnLayoutListener(this);
                            else vto.removeOnGlobalLayoutListener(this);

                            // create and add statusbar background view
                            final Drawable statusBarBackground = res.getDrawable(R.drawable.statusbar_background);
                            if (statusBarBackground == null) throw new IllegalStateException(
                                    "The attribute R.drawable.statusbar_background must be set.");
                            final int statusBarHeight =
                                    ((ViewGroup.MarginLayoutParams)actionBarContainer.getLayoutParams()).topMargin;
                            final View statusBarView = new View(ExtendedActionBarActivity.this);
                            setBackground(statusBarView, statusBarBackground);
                            final FrameLayout.LayoutParams statusBarBackground_lp = new FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight,
                                    Gravity.TOP | Gravity.FILL_HORIZONTAL);
                            contentView.addView(statusBarView, 0, statusBarBackground_lp);

                            // shift content under actionbar
                            final ViewGroup.MarginLayoutParams content_lp =
                                    (ViewGroup.MarginLayoutParams)content.getLayoutParams();
                            content_lp.topMargin = getActionBar().getHeight() + statusBarHeight;
                            content.setLayoutParams(content_lp);
                        }
                    });
                }
            }
        }
    }

    /**
     * Find a view by providing the id resource name and package in the form {@code @package/id:name}.
     * <p>
     * Useful for getting "private" views like {@code @android:id/action_bar_container}.
     * </p>
     *
     * @param name the resource id name (for example @android.id:resource would be "resource").
     * @param pkg  the resource package (for example @android.id:resource would be "android").
     *
     * @return The view if found and data is valid, or {@code null}.
     */
    public View findViewById(String name, String pkg)
    {
        final int id = getResources().getIdentifier(name, "id", pkg);
        return id > 0 ? findViewById(id) : null;
    }

    /**
     * Uses {@link android.view.View#setBackground(android.graphics.drawable.Drawable)}
     * if SDK >= 16, else falls back on
     * {@link android.view.View#setBackgroundDrawable(android.graphics.drawable.Drawable)}.
     *
     * @param background the {@code Drawable} to use as the background, or null to remove the background.
     */
    private static void setBackground(View view, Drawable background)
    {
        if (Build.VERSION.SDK_INT < 16) view.setBackgroundDrawable(background);
        else view.setBackground(background);
    }
}
