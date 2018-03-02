package com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment.fragment;


import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shumidub.todoapprealm.App;
import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmcontrollers.FolderTaskRealmController;
import com.shumidub.todoapprealm.realmcontrollers.taskcontroller.TasksRealmController;
import com.shumidub.todoapprealm.realmmodel.FolderTaskObject;
import com.shumidub.todoapprealm.realmmodel.RealmFoldersContainer;
import com.shumidub.todoapprealm.realmmodel.TaskObject;
import com.shumidub.todoapprealm.ui.actionmode.EmptyActionModeCallback;
import com.shumidub.todoapprealm.ui.fragment.task_section.folder_panel_sliding_fragment.adapter.FolderOfTaskRecyclerViewAdapter;
import com.shumidub.todoapprealm.ui.activity.main.MainActivity;
import com.shumidub.todoapprealm.ui.actionmode.task.FolderActionModeCallback;
import com.shumidub.todoapprealm.ui.fragment.task_section.small_tasks_fragment.SmallTaskFragmentPagerAdapter;
import com.shumidub.todoapprealm.ui.dialog.task_folder_dialog.AddFolderDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Calendar;
import java.util.List;
import io.realm.RealmList;

import static com.shumidub.todoapprealm.realmcontrollers.FolderTaskRealmController.listOfFolderIsEmpty;


/**
 * Created by Артем on 19.12.2017.
 */

public class FolderSlidingPanelFragment extends Fragment implements IViewFolderSlidingPanelFragment {

    LinearLayout rootLayout;
    LinearLayout emptyState;
    View view;

    ConstraintLayout.LayoutParams layoutParams;
    int keyboardHeight = 500;

    // ACTIONBAR AND ACTIONMODE
    ActionBar actionBar;
    static ActionMode actionMode;
    ActionMode.Callback folderCallback;
    static final int FOLDER_ACTIONMODE = 2;


    // SLIDING VIEW
    public SlidingUpPanelLayout slidingUpPanelLayout;
    LinearLayout llBottomSmallTasksLabel;

    //////////////////////////////     FOLDERS VIEW AND VARIABLES     ////////////////////////////

    // BOTTOM PANEL
    EditText et;
    TextView tvTaskCountValue;
    TextView tvTaskMaxAccumulate;
    TextView tvTaskPriority;
    TextView tvTaskCycling;
    TextView tvBottomText;

    // FOLDER RV
    RecyclerView rvFolders;
    FolderOfTaskRecyclerViewAdapter folderOfTaskRVAdapter;
    LinearLayoutManager llm;

    // FOLDER LISTENERS
    FolderOfTaskRecyclerViewAdapter.OnHolderTextViewOnClickListener onHolderTextViewOnClickListener;
    FolderOfTaskRecyclerViewAdapter.OnHolderTextViewOnLongClickListener onHolderTextViewOnLongClickListener;


    // FOLDER VARIABLES, DATA
    RealmFoldersContainer realmFoldersContainer;
//    RealmResults<FolderObject> folderObjects;
    RealmList<FolderTaskObject> folderObjects;
    public static Long idFolderFromTag;
    private static String title;
    public static String titleFolder;

    ////////////////////////////     SMALL TASKS VIEWS AND VARIABLES     //////////////////////////

    ViewPager smallTasksViewPager;
    SmallTaskFragmentPagerAdapter smallTaskFragmentPagerAdapter;

    //default values
    int priority = 0;
    boolean cycling = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetTasksCountAccumulation();
        App.folderSlidingPanelFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.slide_up_panel_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);






        ///////////////////////    ACTION BAR, MODE (onViewCreated)    ///////////////////////
        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        setTitle("Tasks");

//        setDayScopeValue();
//        actionBar.setSubtitle("" + App.dayScope);

        ///////////////////////    SLIDING VIEWS (onViewCreated)     ///////////////////////
        slidingUpPanelLayout = view.findViewById(R.id.slidingup_panel_layout);
        llBottomSmallTasksLabel = view.findViewById(R.id.ll_footer);

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        llBottomSmallTasksLabel.setVisibility(View.VISIBLE);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        slidingUpPanelLayout.setStateListAnimator(new StateListAnimator());
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                llBottomSmallTasksLabel.setAlpha(1.0f - slideOffset);
                if (slideOffset > 0.87){
                    llBottomSmallTasksLabel.setVisibility(View.GONE);
                }else if (slideOffset<0.85){
                    llBottomSmallTasksLabel.setVisibility(View.VISIBLE);
                }

                if (slideOffset > 0.3 && slideOffset < 0.7) finishActionMode();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) setTitle("Tasks");
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    finishActionMode();
                    setTitle(FolderTaskRealmController.getFoldersList().get(smallTasksViewPager.getCurrentItem()).getName());
                }
            }
        });

        ///////////////////////    FOLDER VIEWS (onViewCreated)     //////////////////////
        findFolderViews(view);
        llm = new LinearLayoutManager(getContext());
        rvFolders.setLayoutManager(llm);
        folderObjects = FolderTaskRealmController.getFoldersList();

        //set empty state for folder // todo need redesign view
        setEmptyStateIfFoldersIsEmpty(view);

        //set adapter for folder rv
        folderOfTaskRVAdapter = new FolderOfTaskRecyclerViewAdapter(folderObjects, getActivity());
        rvFolders.setAdapter(folderOfTaskRVAdapter);
        //todo empty state

        emptyState = view.findViewById(R.id.ll_empty_state);
        setEmptyStateIfNeed();


        // SET LISTENERS
        onHolderTextViewOnClickListener =
            (FolderOfTaskRecyclerViewAdapter.ViewHolder holder, int position) -> {

                finishActionMode();

                String text = et.getText().toString();
                int count = Integer.valueOf(tvTaskCountValue.getText().toString());
                int maxAccumulation = Integer.valueOf(tvTaskMaxAccumulate.getText().toString());

                if (!text.isEmpty() || !text.equals("")) {

                    idFolderFromTag = (Long) holder.itemView.findViewById(R.id.tv_note_text).getTag();

                    TasksRealmController.addTask(text, count , maxAccumulation, cycling, priority,
                            idFolderFromTag);
//                            ((Long) holder.itemView.findViewById(R.id.item_text).getTag()) );

                    //todo reset view
                    priority = 0;
                    cycling = false;
                    et.setText("");

                    folderOfTaskRVAdapter.notifyDataSetChanged();

                } else {


//                    idFolderFromTag = (Long) holder.itemView.findViewById(R.id.item_text)getTag();
                    idFolderFromTag = (Long) holder.itemView.findViewById(R.id.tv_note_text).getTag();

                    // setTasks(); //todo check if it need


                    smallTaskFragmentPagerAdapter = new SmallTaskFragmentPagerAdapter(getActivity().getSupportFragmentManager());
                    smallTasksViewPager.setAdapter(smallTaskFragmentPagerAdapter);
                    smallTasksViewPager.setCurrentItem(position);



                    setTitle(FolderTaskRealmController.getFolder(idFolderFromTag).getName());
                    slidingUpPanelLayout. setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);


                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);



                }
            };

        onHolderTextViewOnLongClickListener
            = (FolderOfTaskRecyclerViewAdapter.ViewHolder holder, int position) -> {
//            idFolderFromTag = (Long) holder.itemView.findViewById(R.id.item_text).getTag();
            idFolderFromTag = (Long) holder.itemView.findViewById(R.id.tv_note_text).getTag();
            titleFolder = FolderTaskRealmController.getFolder(idFolderFromTag).getName();
            finishActionMode();
            actionMode = getActivity().startActionMode(getCallback(FOLDER_ACTIONMODE));

            /*
            todo 1 need delete smalltaskfragment after delete list and need reset actionmode
            todo 2 and запрет открытия слайдин панели если нулл или может быть удален лист
            */
        };


        folderOfTaskRVAdapter.setOnHolderTextViewOnClickListener(onHolderTextViewOnClickListener);
        folderOfTaskRVAdapter.setOnHolderTextViewOnLongClickListener(onHolderTextViewOnLongClickListener);


        // set ITEM TOUCH HELPER for folder rv
        App.initRealm();
        realmFoldersContainer = App.realm.where(RealmFoldersContainer.class).findFirst();
        //todo try is it working, or need not use linked variable
        RealmList<FolderTaskObject> folderOfTasksLis = realmFoldersContainer.folderOfTasksList;
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP | ItemTouchHelper.DOWN ,0) {

            int dragFrom = -1;
            int dragTo = -1;

            @SuppressLint("RestrictedApi")
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                actionBar.startActionMode(new EmptyActionModeCallback());

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (dragFrom == -1) {
                    dragFrom = fromPosition;
                }
                dragTo = toPosition;

                folderOfTaskRVAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            // todo need fix if move bellow "add list"
            private void reallyMoved(int from, int to) {
                App.initRealm();
                if (from < folderOfTasksLis.size()){
                    App.realm.executeTransaction((realm) -> {
                        int to2 = to<folderOfTasksLis.size() ? to : folderOfTasksLis.size()-1;
                        folderOfTasksLis.add(to2, folderOfTasksLis.remove(from));
                    });
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                reallyMoved(dragFrom, dragTo);
                }
                dragFrom = dragTo = -1;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) { }

        });

        itemTouchHelper.attachToRecyclerView(rvFolders);


        ///////////////////////    SMALL TASKS VIEWS (onViewCreated)     //////////////////////
        smallTasksViewPager = view.findViewById(R.id.view_pager_small_tasks);
        smallTaskFragmentPagerAdapter = new SmallTaskFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        smallTasksViewPager.setAdapter(smallTaskFragmentPagerAdapter);
        smallTasksViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                finishActionMode();
                setTitle(FolderTaskRealmController.getFoldersList().get(position).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        //set root view changer on keyboard opens

        layoutParams
                = (ConstraintLayout.LayoutParams) rvFolders.getLayoutParams();
        rootLayout = ((MainActivity)getActivity()).rootLayout;
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = rootLayout.getHeight();
//                Log.w("Foo", String.format("layout height: %d", height));
                Rect r = new Rect();
                rootLayout.getWindowVisibleDisplayFrame(r);
                int visible = r.bottom - r.top;
                if (height - visible > 200){
                    keyboardHeight = height - visible;
                }
//                Log.w("Foo", String.format("visible height: %d", visible));
//                Log.w("Foo", String.format("keyboard height: %d", height - visible));
            }
        });



        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                //todo need thing about logig
                (boolean isOpen) -> {
                        if (isOpen && slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED && et.isFocused()  ){




                            MainActivity activity = (MainActivity) getActivity();

                            int topMargin = keyboardHeight - tvBottomText.getHeight() + activity.getPixelsFromDPs(4);

                            activity.setPageCanChangedScrolled(false);
                            layoutParams.setMargins(0,topMargin,0,0);
                            rvFolders.setLayoutParams(layoutParams);

                        }
                        else {
                            ((MainActivity)getActivity()).setPageCanChangedScrolled(true);
                            layoutParams.setMargins(0,0,0,0);
                            rvFolders.setLayoutParams(layoutParams);
                        }
                });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem add = menu.add(2,2,2,"add ");
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        add.setIcon(R.drawable.ic_add);
        add.setOnMenuItemClickListener((v)->{
            if (!(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)) {
                AddFolderDialog addFolderDialog = new AddFolderDialog();
                addFolderDialog.show(getActivity().getSupportFragmentManager(), "addfolder");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInputFromWindow(
                        getActivity().getWindow().getDecorView().getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
         return true;});
    }

    //FOLDER
    /** Find folders view */
    private void findFolderViews(View view){
        rvFolders = view.findViewById(R.id.rv_lists);
        et = view.findViewById(R.id.et);


        tvTaskCycling = view.findViewById(R.id.task_cycling);
        tvTaskPriority = view.findViewById(R.id.task_priority);

        tvTaskCountValue = view.findViewById(R.id.task_value);
        tvTaskCountValue.setOnClickListener((v) -> onTaskValueClick(tvTaskCountValue));

        tvTaskMaxAccumulate = view.findViewById(R.id.task_max_accumulate);
        tvTaskMaxAccumulate.setOnClickListener((v) -> onTaskValueClick(tvTaskMaxAccumulate));

        tvTaskPriority.setOnClickListener((v) -> onTaskPriorityClick(tvTaskPriority));
        tvTaskCycling.setOnClickListener((v) -> onTaskCyclingClick(tvTaskCycling));

        tvBottomText = view.findViewById(R.id.bottom_text);



    }

    private void setEmptyStateIfFoldersIsEmpty(View view){
        if (listOfFolderIsEmpty()){
            (view.findViewById(R.id.tv_empty)).setVisibility(View.VISIBLE);
        } else (view.findViewById(R.id.tv_empty)).setVisibility(View.INVISIBLE);
    }

    private ActionMode.Callback getCallback(int callbackType){
        if (callbackType == FOLDER_ACTIONMODE) {
            folderCallback = new FolderActionModeCallback().getListActionModeCallback(
                    (MainActivity) getActivity(), this, idFolderFromTag);
            return folderCallback;
        }
        else return null;
    }


    //////////////////////    BOTTOM PANEL ADD TASK ON SLIDING SCREEN    //////////////////////
    public void onTaskValueClick(TextView view) {
        int i = Integer.valueOf(view.getText().toString());
        if (i<10){
            i++;
        }else if (i>9){
            i=1;
        }
        view.setText("" + i);

        if (i<2) view.setTextColor(getResources().getColor(R.color.colorWhite));
        else view.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    public void onTaskPriorityClick(View view) {
        if (priority>2) priority =0;
        else priority ++;

        if (priority>1){
            String text = "!";
            int i = priority;
            while (i>1){
                text +="!";
                i--;
            }
            ((TextView) view).setText(text);
        } else ((TextView) view).setText("!");

        if (priority>0) ((TextView) view).setTextColor(getResources().getColor(R.color.colorAccent));
        else ((TextView) view).setTextColor(getResources().getColor(R.color.colorWhite));
    }

    public void onTaskCyclingClick(View view) {
        cycling = !cycling;
        if (cycling) ((TextView) view).setTextColor(getResources().getColor(R.color.colorAccent));
        else ((TextView) view).setTextColor(getResources().getColor(R.color.colorWhite));
    }

    public void dataChanged(){
        onResume();
    }

    public void notifyListsDataChanged(){
//      folderObjects = FolderRealmController.getFolders();
//      folderOfTaskRVAdapter.notifyDataSetChanged();
        folderOfTaskRVAdapter.notifyDataSetChanged();
        smallTaskFragmentPagerAdapter.notifyDataSetChanged();
        setEmptyStateIfNeed();
    }


    public void notifyFolderOfTasksRVAdapterDataSetChanged(){
        folderOfTaskRVAdapter.notifyDataSetChanged();
    }

//    protected void tasksDataChanged(){
//        setTasks();
//    }

    public void finishActionMode(){
        ((MainActivity) getActivity()).startSupportActionMode(new EmptyActionModeCallback());
    }


    public static int getDayScopeValue(){return App.dayScope;}


    /** update done status and number of doing on cycling tasks if done day != today*/
    private void resetTasksCountAccumulation(){
        // done and not done tasks but where countAccumulation more than 0
        List<TaskObject> allDoneAndParticullaryDoneTasks = TasksRealmController.getDoneAndPartiallyDoneTasks();

        int todayDate = Integer.valueOf("" + Calendar.getInstance().get(Calendar.DAY_OF_YEAR) +
                Calendar.getInstance().get(Calendar.YEAR));

        for (TaskObject task : allDoneAndParticullaryDoneTasks) {
            if (task.isCycling()  && task.getLastDoneDate() != todayDate ){
                TasksRealmController.setTaskDoneOrParticullaryDone(task, false);
            }
        }
    }

    public static String getTitle(){
        if (title != null && !title.isEmpty()) return title;
        else return "Tasks";
    }

    private void setTitle(String title){
        this.title = title;
        ((MainActivity) getActivity()).getSupportActionBar()
                        .setTitle(title);
    }


    private void setEmptyStateIfNeed(){

        RecyclerView.Adapter adapter = rvFolders.getAdapter();

        if (adapter.getItemCount() == 0){
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }


    public String getValidTitle(){

        if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            return FolderTaskRealmController.getFoldersList().get(smallTasksViewPager.getCurrentItem()).getName();
        } else return "Tasks";

    }


    // todo thinc about logic открытия таск панели если нет фолдеров или какой по умоллчанию откроется (видимо откроется первый и использовать эмпти стэйт или запретить экспандить панель)

}







