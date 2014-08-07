package com.afollestad.silk.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.silk.R;

import java.util.ArrayList;
import java.util.List;

public class SilkDialog extends DialogFragment implements View.OnClickListener {

    public SilkDialog() {
    }

    public static SilkDialog create(Activity context) {
        return create(context, false);
    }

    public static SilkDialog create(Activity context, boolean darkTheme) {
        SilkDialog dialog = new SilkDialog();
        dialog.mContext = context;
        Bundle args = new Bundle();
        args.putBoolean("dark_theme", darkTheme);
        dialog.setArguments(args);
        return dialog;
    }

    private Activity getContext() {
        if (getActivity() != null) mContext = getActivity();
        return mContext;
    }

    protected Activity mContext;
    private boolean mDarkTheme;
    private int mIcon;
    private CharSequence mTitle;
    private CharSequence mMessage;
    private int mAccentColor;
    private String mPositiveText;
    private String mNeutralText;
    private String mNegativeText;
    private String[] mItems;
    private String mInputHint;
    private String mInputPrefill;
    private float lineSpacingMultiplier = 1.4f;
    private int mTextColor;

    private boolean mInput;
    private boolean mSingleChoice;
    private boolean mMultiChoice;
    private int preChoice;

    private DialogCallback mCallback;
    private InputCallback mInputCallback;
    private SelectionCallback mSelectionCallback;
    private MultiSelectionCallback mMultiChoiceCallback;
    private DismissCallback mDismissCallback;

    private View customView;
    private EditText inputView;
    private LinearLayout listView;

    public SilkDialog setIcon(int icon) {
        mIcon = icon;
        return this;
    }

    public final SilkDialog setTitle(int title) {
        return setTitle(getContext().getString(title));
    }

    public final SilkDialog setTitle(int title, Object... formatArgs) {
        mTitle = getContext().getString(title, formatArgs);
        return this;
    }

    public SilkDialog setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public final SilkDialog setMessage(int message) {
        return setMessage(getContext().getString(message));
    }

    public final SilkDialog setMessage(int message, float lineSpacingMultiplier) {
        return setMessage(getContext().getString(message), lineSpacingMultiplier);
    }

    public final SilkDialog setMessage(int message, Object... formatArgs) {
        return setMessage(getContext().getString(message, formatArgs));
    }

    public final SilkDialog setMessage(int message, float lineSpacingMultiplier, Object... formatArgs) {
        return setMessage(getContext().getString(message, formatArgs), lineSpacingMultiplier);
    }

    public SilkDialog setMessage(CharSequence message) {
        mMessage = message;
        return this;
    }

    public SilkDialog setMessage(CharSequence message, float lineSpacingMultiplier) {
        mMessage = message;
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        return this;
    }

    public SilkDialog setAccentColor(int color) {
        mAccentColor = color;
        return this;
    }

    public final SilkDialog setAccentColorRes(int colorRes) {
        return setAccentColor(getContext().getResources().getColor(colorRes));
    }

    public final SilkDialog setPostiveButtonText(int text) {
        return setPostiveButtonText(getContext().getString(text));
    }

    public SilkDialog setPostiveButtonText(String text) {
        mPositiveText = text;
        return this;
    }

    public final SilkDialog setNeutralButtonText(int text) {
        return setNeutralButtonText(getContext().getString(text));
    }

    public SilkDialog setNeutralButtonText(String text) {
        mNeutralText = text;
        return this;
    }

    public final SilkDialog setNegativeButtonText(int text) {
        return setNegativeButtonText(getContext().getString(text));
    }

    public SilkDialog setNegativeButtonText(String text) {
        mNegativeText = text;
        return this;
    }

    public final SilkDialog setButtonListener(DialogCallback callback) {
        mCallback = callback;
        return this;
    }

    public final SilkDialog setDismissListener(DismissCallback callback) {
        mDismissCallback = callback;
        return this;
    }

    public SilkDialog setItems(String[] items, SelectionCallback callback) {
        if (mInput)
            throw new IllegalStateException("You cannot accept input and display a list at the same time.");
        mItems = items;
        mSelectionCallback = callback;
        return this;
    }

    public final SilkDialog setItems(int stringArrayRes, SelectionCallback callback) {
        return setItems(getContext().getResources().getStringArray(stringArrayRes), callback);
    }

    public SilkDialog setSingleChoiceItems(String[] items, int preselect, SelectionCallback callback) {
        mSingleChoice = true;
        mMultiChoice = false;
        preChoice = preselect;
        return setItems(items, callback);
    }

    public SilkDialog setSingleChoiceItems(int stringArrayRes, int preselect, SelectionCallback callback) {
        mSingleChoice = true;
        mMultiChoice = false;
        preChoice = preselect;
        return setItems(stringArrayRes, callback);
    }

    public SilkDialog setMultiChoiceItems(String[] items, MultiSelectionCallback callback) {
        if (mInput)
            throw new IllegalStateException("You cannot accept input and display a list at the same time.");
        mItems = items;
        mMultiChoiceCallback = callback;
        mSingleChoice = false;
        mMultiChoice = true;
        return this;
    }

    public final SilkDialog setMultiChoiceItems(int stringArrayRes, MultiSelectionCallback callback) {
        return setMultiChoiceItems(getContext().getResources().getStringArray(stringArrayRes), callback);
    }

    public SilkDialog setAcceptsInput(boolean acceptsInput, String inputHint, String prefill, InputCallback callback) {
        if (mItems != null)
            throw new IllegalStateException("You cannot accept input and display a list at the same time.");
        mInput = acceptsInput;
        mInputHint = inputHint;
        mInputPrefill = prefill;
        mInputCallback = callback;
        return this;
    }

    public final SilkDialog setAcceptsInput(boolean acceptsInput, int inputHint, int prefill, InputCallback callback) {
        String hint = null;
        String prefillText = null;
        if (inputHint != 0)
            hint = getContext().getString(inputHint);
        if (prefill != 0)
            prefillText = getContext().getString(prefill);
        return setAcceptsInput(acceptsInput, hint, prefillText, callback);
    }

    public final SilkDialog setAcceptsInput(boolean acceptsInput, String inputHint, InputCallback callback) {
        return setAcceptsInput(acceptsInput, inputHint, null, callback);
    }

    public SilkDialog setCustomView(View view) {
        customView = view;
        return this;
    }

    public final SilkDialog setCustomView(int layoutRes) {
        return setCustomView(LayoutInflater.from(getContext()).inflate(layoutRes, null));
    }

    public final void setCanSubmit(boolean canSubmit) {
        if (getDialog() == null) throw new IllegalStateException("View not created yet.");
        getDialog().findViewById(android.R.id.button1).setEnabled(canSubmit);
    }

    public final TextView getMessageView() {
        return (TextView) getDialog().findViewById(android.R.id.message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        mDarkTheme = getArguments().getBoolean("dark_theme");

        if (mDarkTheme) {
            mTextColor = getContext().getResources().getColor(android.R.color.white);
        } else {
            mTextColor = getContext().getResources().getColor(android.R.color.black);
        }
        if (mAccentColor == 0) {
            mAccentColor = mTextColor;
        }
        if (mPositiveText == null) {
            mPositiveText = getContext().getString(android.R.string.ok);
        }

        Context context = new ContextThemeWrapper(getContext(), mDarkTheme ? android.R.style.Theme_Holo_Dialog_NoActionBar :
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        Dialog dialog = new Dialog(context,
                mDarkTheme ? android.R.style.Theme_Holo_Dialog_NoActionBar :
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar
        );
        dialog.setContentView(R.layout.dialog_layout);

        ImageView icon = (ImageView) dialog.findViewById(android.R.id.icon);
        if (mIcon != 0) {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(mIcon);
        } else {
            icon.setVisibility(View.GONE);
        }

        TextView title = (TextView) dialog.findViewById(android.R.id.title);
        title.setText(mTitle);
        title.setTextColor(mAccentColor);
        dialog.findViewById(R.id.titleDivider).setBackgroundColor(mAccentColor);

        TextView message = (TextView) dialog.findViewById(android.R.id.message);
        if (mMessage != null && !TextUtils.isEmpty(mMessage)) {
            message.setTextColor(mTextColor);
            message.setText(mMessage);
        } else {
            message.setVisibility(View.GONE);
        }
        message.setLineSpacing(0.0f, lineSpacingMultiplier);

        Button positive = (Button) dialog.findViewById(android.R.id.button1);
        positive.setText(mPositiveText);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit();
            }
        });

        Button neutral = (Button) dialog.findViewById(android.R.id.button3);
        if (mNeutralText != null) {
            neutral.setText(mNeutralText);
            neutral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (mCallback != null) mCallback.onNeutral();
                }
            });
        } else neutral.setVisibility(View.GONE);

        Button negative = (Button) dialog.findViewById(android.R.id.button2);
        if (mNegativeText != null) {
            negative.setText(mNegativeText);
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if (mCallback != null) mCallback.onCancelled();
                    if (mInputCallback != null) mInputCallback.onCancelled();
                    if (mSelectionCallback != null) mSelectionCallback.onCancelled();
                }
            });
        } else negative.setVisibility(View.GONE);

        LinearLayout childArea = (LinearLayout) dialog.findViewById(android.R.id.primary);
        if (customView != null) {
            childArea.setVisibility(View.VISIBLE);
            childArea.removeAllViews();
            childArea.addView(customView);
        } else if (mInput || mItems != null) {
            childArea.setVisibility(View.VISIBLE);
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (mInput) {
                View inputArea = inflater.inflate(R.layout.dialog_input, null);
                if (mMessage != null) {
                    message.setPadding(message.getPaddingLeft(), message.getPaddingTop(),
                            message.getPaddingRight(), 0);
                } else {
                    final int paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources()
                            .getDisplayMetrics());
                    inputArea.setPadding(inputArea.getPaddingLeft(), paddingTop,
                            inputArea.getPaddingRight(), inputArea.getPaddingBottom());
                }
                inputView = (EditText) inputArea.findViewById(android.R.id.input);
                if (mInputHint != null) inputView.setHint(mInputHint);
                if (mInputPrefill != null) inputView.append(mInputPrefill);
                childArea.addView(inputArea);
            }

            if (mItems != null) {
                listView = (LinearLayout) inflater.inflate(R.layout.dialog_list, null);
                for (int i = 0; i < mItems.length; i++) {
                    View view;
                    if (mSingleChoice) {
                        view = inflater.inflate(R.layout.dialog_listitem_radio, null);
                        RadioButton radio = (RadioButton) view.findViewById(R.id.radio);
                        if (preChoice == i) radio.setChecked(true);
                        radio.setText(mItems[i]);
                        radio.setTextColor(mTextColor);
                        radio.setTag(i);
                        radio.setOnClickListener(this);
                    } else if (mMultiChoice) {
                        view = inflater.inflate(R.layout.dialog_listitem_checkbox, null);
                        CheckBox check = (CheckBox) view.findViewById(R.id.check);
                        check.setText(mItems[i]);
                        check.setTextColor(mTextColor);
                        check.setTag(i);
                        check.setOnClickListener(this);
                    } else {
                        view = inflater.inflate(R.layout.dialog_listitem, null);
                        ((TextView) view).setText(mItems[i]);
                        ((TextView) view).setTextColor(mTextColor);
                        view.setTag(i);
                        view.setOnClickListener(this);
                    }
                    listView.addView(view);
                }
                childArea.addView(listView);
                if (!mMultiChoice)
                    dialog.findViewById(android.R.id.candidatesArea).setVisibility(View.GONE);
            }
        }

        return dialog;
    }

    @Override
    public void onClick(View view) {
        if (view != null && view.getTag() instanceof Integer) {
            if (mMultiChoice) {
                CheckBox check = (CheckBox) view.findViewById(R.id.check);
                check.setChecked(!check.isChecked());
            } else if (mSelectionCallback != null) {
                int index = (Integer) view.getTag();
                mSelectionCallback.onSelection(index, mItems[index]);
                onSubmit();
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (mCallback != null) mCallback.onCancelled();
        if (mInputCallback != null) mInputCallback.onCancelled();
        if (mSelectionCallback != null) mSelectionCallback.onCancelled();
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mDismissCallback != null) mDismissCallback.onDismissed();
    }

    public final SilkDialog show() {
        return show(false);
    }

    public SilkDialog show(boolean cancelable) {
        if (getContext() == null)
            throw new RuntimeException("Context is null, make sure you used the static create() method rather than the constructor to instantiate your dialog.");
        setCancelable(cancelable);
        show(getContext().getFragmentManager(), "CUSTOMALERT:" + mTitle);
        return this;
    }

    protected void onSubmit() {
        dismiss();
        if (mCallback != null) mCallback.onPositive();
        if (inputView != null && mInputCallback != null)
            mInputCallback.onInput(inputView.getText().toString().trim());
        if (mMultiChoice && mMultiChoiceCallback != null) {
            List<Integer> selectedIndices = new ArrayList<Integer>();
            List<String> selectedValues = new ArrayList<String>();
            for (int i = 0; i < listView.getChildCount(); i++) {
                CheckBox check = (CheckBox) listView.getChildAt(i).findViewById(R.id.check);
                if (check.isChecked()) {
                    selectedIndices.add(i);
                    selectedValues.add(check.getText().toString());
                }
            }
            mMultiChoiceCallback.onSelection(
                    selectedIndices.toArray(new Integer[selectedIndices.size()]),
                    selectedValues.toArray(new String[selectedValues.size()])
            );
        }
    }

    public static abstract class DialogCallback {
        public abstract void onPositive();

        public void onNeutral() {
            // Do nothing by default
        }

        public void onCancelled() {
            // Do nothing by default
        }
    }

    public static abstract class InputCallback {
        public abstract void onInput(String input);

        public void onCancelled() {
            // Do nothing by default
        }
    }

    public static abstract class SelectionCallback {
        public abstract void onSelection(int index, String value);

        public void onCancelled() {
            // Do nothing by default
        }
    }

    public static abstract class MultiSelectionCallback {
        public abstract void onSelection(Integer[] indices, String[] values);

        public void onCancelled() {
            // Do nothing by default
        }
    }

    public static abstract interface DismissCallback {
        public abstract void onDismissed();
    }
}
