MaterialEditText
====================
This is an EditText that implements floating hints and inline errors, to match the material design guidelines.

![Example guideline image][example]

Usage
-------
Simply use in place of a normal EditText (its a subclass, so should be a drop-in replacement).

    <com.trogdor.widgets.MaterialEditText
        android:id="@+id/email"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/prompt_email"
        android:inputType="textEmailAddress"
        android:maxLines="1"
        android:singleLine="true"
        android:layout_marginBottom="22dp">

To trigger the error state, use the normal .setError() method:
    mEmailView.setError("This field is required!");

And to clear it:
    mEmailView.setError(null)

The errorColor attribute can be used to change the color of the line and error text in error state.

License
-------

    This Source Code Form is subject to the terms of the Mozilla Public
    License, v. 2.0. If a copy of the MPL was not distributed with this
    file, You can obtain one at http://mozilla.org/MPL/2.0/.

[example]: http://material-design.storage.googleapis.com/publish/v_2/material_ext_publish/0Bx4BSt6jniD7TjdLdmlic3BsRUk/patterns_errors_userinput12.png
 
Credits
-------
Code based off [thebnich/FloatingHintEditText][fork]
Original concept: http://mattdsmith.com/float-label-pattern/

[fork]: https://github.com/thebnich/FloatingHintEditText
