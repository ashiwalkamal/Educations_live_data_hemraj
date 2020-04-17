package com.attors.educations.Modal;

import android.widget.EditText;

public class Validation_custome {
    String type;
    EditText editText;

    public Validation_custome(String type, EditText editText) {
        this.type = type;
        this.editText = editText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }
}
