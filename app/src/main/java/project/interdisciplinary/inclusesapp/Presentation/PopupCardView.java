package project.interdisciplinary.inclusesapp.Presentation;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import project.interdisciplinary.inclusesapp.R;

public class PopupCardView {

    private Context context;
    private AlertDialog dialog;
    private OnOptionSelectedListener listener;

    public PopupCardView(Context context) {
        this.context = context;
    }

    public void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup_confirm_alterations, null);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        Button btYes = view.findViewById(R.id.yesBtn);
        Button btNo = view.findViewById(R.id.noBtn);

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOptionSelected(true);
                }
                dialog.dismiss();
            }
        });

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOptionSelected(false);
                }
                dialog.dismiss();
            }
        });
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnOptionSelectedListener {
        void onOptionSelected(boolean optionSelected);
    }
}
