package gte.com.bkhtaskmanager

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.custom_chip.view.*

class ChipWithImage : LinearLayout {
    /** Dili ni official Chip class sa Android.
     *  This is a custom chip to enable CircleImageView to be shown as a profile picture (para chuy)
     */

    constructor(context: Context)
            : this(context, null)

    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.custom_chip, this)
        val attributes = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ChipWithImage,
                defStyleAttr, 0)
        try {
            text = attributes.getString(R.styleable.ChipWithImage_text)
            imageResource = attributes.getDrawable(R.styleable.ChipWithImage_imageSrc)
            imageURL = attributes.getString(R.styleable.ChipWithImage_imageURL)

            toggleRemoveIcon()
        } finally {
            attributes.recycle()
        }
    }



    var text: String? = null
        /**
         * Sets the text label for this ChipView
         * @param value The string to set for this label
         */
        set(value) {
            field = value
            displayText()
        }

    var imageResource: Drawable? = null
        /**
         * Sets the image for this ChipView
         * @param value The drawable to set for this chip image
         */
        set(value) {
            field = value
            displayImage()
        }

    var imageURL: String? = null
        /**
         * Sets the image for this ChipView to be loaded from the given URL / path
         * @param value The URL path to an image to set for this chip
         */
        set(value) {
            field = value
            displayImage()
        }

    private var removeListener: OnChipRemovedListener? = null

    private fun displayText() {
        if (text != null)
            chip_text.text = text
        else
            chip_text.text = ""
        chip_text.invalidate()
        chip_text.requestLayout()
    }

    private var isCloseIconShown: Boolean = false

    /**
     * Displays a new image.
     */
    private fun displayImage() {
        when {
            imageURL != null -> {
                chip_image.visibility = View.VISIBLE
                chip_image.load(imageURL!!)
            }
            imageResource != null -> {
                chip_image.visibility = View.VISIBLE
                chip_image.setImageDrawable(imageResource)
            }
            else -> {
                chip_image.visibility = View.GONE
                chip_image.setImageDrawable(null)
            }
        }
        chip_image.invalidate()
        chip_image.requestLayout()
    }

    /**
     * Display/Remove the remove icon.
     */
    fun toggleRemoveIcon() {
        if (isCloseIconShown) {
            chip_close.visibility = View.GONE
            isCloseIconShown = false
        } else {
            chip_close.visibility = View.VISIBLE
            isCloseIconShown = true
        }

        chip_close.setOnClickListener {
            removeListener!!.onRemove(this)
        }
        chip_close.invalidate()
        chip_close.requestLayout()
    }

    /**
     * Sets the OnClickListener or function to be called when the remove / close
     * button on the ChipView is clicked
     * @param listener The listener to be executed when the close button is clicked
     */
    fun setOnRemoveListener(listener: OnChipRemovedListener?) {
        removeListener = listener
    }

    private fun CircleImageView?.load(path: String) {
        Picasso.get().load(path).fit().into(this)
    }

    interface OnChipRemovedListener {
        /**
         * Called when a ChipView remove button has been clicked
         *
         * @param v The ChipView that the user interacted with.
         */
        fun onRemove(v: View)
    }
}