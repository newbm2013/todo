package com.shumidub.todoapprealm.ui.dialog.report_dialog;

/**
 * Created by A.shumidub on 05.02.18.
 */

public class AddReportDialog extends ReportDialog {

    @Override
    public void setTitle() {
       title = ADD_REPORT_TITLE;
    }

    @Override
    public void setView() {
        super.setView();
    }

    @Override
    public void setPositiveButtonText() {
        positiveButtonText = ADD_BUTTON_TEXT;
    }

    @Override
    public void setPositiveButtonInterface() {
        positiveButtonInterface = (dialogInterface, i)-> {
            //    String text = ((EditText) getDialog().findViewById(R.id.name)).getText().toString();
//                    if (!text.isEmpty()){
//                            long idFolder = FolderRealmController.addFolder(text);
//                            Toast.makeText(getContext(),"Done", Toast.LENGTH_SHORT).show();
//                            activity = (MainActivity) getActivity();
//                            for (Fragment fragment : activity.getSupportFragmentManager().getFragments()){
//                            if (fragment instanceof FolderSlidingPanelFragment){
//                            ((FolderSlidingPanelFragment) fragment).notifyListsDataChanged();
//                            }
//                            }
//                            }
        };
    }
}
