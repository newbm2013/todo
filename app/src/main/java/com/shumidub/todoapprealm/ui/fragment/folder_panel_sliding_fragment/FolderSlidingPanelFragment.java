package com.shumidub.todoapprealm.ui.fragment.folder_panel_sliding_fragment;


import android.animation.StateListAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmmodel.FolderObject;
import com.shumidub.todoapprealm.realmmodel.RealmInteger;
import com.shumidub.todoapprealm.realmmodel.TaskObject;
import com.shumidub.todoapprealm.ui.actionmode.EmptyActionModeCallback;
import com.shumidub.todoapprealm.ui.fragment.small_tasks_fragment.SmallTasksFragment;

import com.shumidub.todoapprealm.realmcontrollers.FolderRealmController;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.ui.activity.main_activity.MainActivity;
import com.shumidub.todoapprealm.ui.actionmode.FolderActionModeCallback;
import com.shumidub.todoapprealm.ui.fragment.small_tasks_fragment.SmallTaskFragmentPagerAdapter;
import com.shumidub.todoapprealm.ui.folder_dialog.AddFolderDialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

import static com.shumidub.todoapprealm.realmcontrollers.FolderRealmController.listOfFolderIsEmpty;

/**
 * Created by Артем on 19.12.2017.
 */

public class FolderSlidingPanelFragment extends Fragment {

    // ACTIONBAR AND ACTIONMODE
    ActionBar actionBar;
    static ActionMode actionMode;
    ActionMode.Callback folderCallback;
    static final int FOLDER_ACTIONMODE = 2;
    public int dayScope;

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

    // FOLDER RV
    RecyclerView rvFolders;
    FolderOfTaskRecyclerViewAdapter folderOfTaskRVAdapter;

    // FOLDER LISTENERS
    FolderOfTaskRecyclerViewAdapter.OnHolderTextViewOnClickListener onHolderTextViewOnClickListener;
    FolderOfTaskRecyclerViewAdapter.OnHolderTextViewOnLongClickListener onHolderTextViewOnLongClickListener;
    FolderOfTaskRecyclerViewAdapter.OnFooterTextViewOnClickListener onFooterTextViewOnClickListener;

    // FOLDER VARIABLES, DATA
    RealmResults<FolderObject> folderObjects;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_slideup_panel_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ///////////////////////    ACTION BAR, MODE (onViewCreated)    ///////////////////////
        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        setTitle("Tasks");

        ///////////////////////    SLIDING VIEWS (onViewCreated)     ///////////////////////
        slidingUpPanelLayout = view.findViewById(R.id.slidingup_panel_layout);
        llBottomSmallTasksLabel = view.findViewById(R.id.ll_footer);

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        llBottomSmallTasksLabel.setVisibility(View.VISIBLE);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        //todo realise
        slidingUpPanelLayout.setStateListAnimator(new StateListAnimator());
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                llBottomSmallTasksLabel.setAlpha(1.0f - slideOffset);
                if (slideOffset > 0.87){
                    llBottomSmallTasksLabel.setVisibility(View.GONE);
                } else if (slideOffset<0.85){
                    llBottomSmallTasksLabel.setVisibility(View.VISIBLE);
                }

                if (slideOffset > 0.3 && slideOffset < 0.7) finishActionMode();
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) setTitle("Tasks");
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) finishActionMode();
            }
        });

        ///////////////////////    FOLDER VIEWS (onViewCreated)     //////////////////////
        findFolderViews(view);
        rvFolders.setLayoutManager(new LinearLayoutManager(getContext()));
        folderObjects = FolderRealmController.getFolders();

        //set empty state for folder // todo need redesign view
        setEmptyStateIfFoldersIsEmpty(view);

        //set adapter for folder rv
        folderOfTaskRVAdapter = new FolderOfTaskRecyclerViewAdapter(folderObjects, getActivity());
        rvFolders.setAdapter(folderOfTaskRVAdapter);


        // SET LISTENERS
        onHolderTextViewOnClickListener =
            (FolderOfTaskRecyclerViewAdapter.ViewHolder holder, int position) -> {

                finishActionMode();

                String text = et.getText().toString();
                int count = Integer.valueOf(tvTaskCountValue.getText().toString());
                int maxAccumulation = Integer.valueOf(tvTaskMaxAccumulate.getText().toString());

                if (!text.isEmpty() || !text.equals("")) {

                    TasksRealmController.addTask(text, count , maxAccumulation, cycling, priority,
                            ((Long) holder.itemView.findViewById(R.id.item_text).getTag()) );

                    //todo reset view
                    priority = 0;
                    cycling = false;
                    et.setText("");
                } else {
                    idFolderFromTag = (Long) holder.itemView.findViewById(R.id.item_text).findViewById(R.id.item_text).getTag();

                    // setTasks(); //todo check if it need

                    Fragment currentFragment = smallTaskFragmentPagerAdapter.getItem(position);
                    if (currentFragment instanceof SmallTasksFragment){
                        ((SmallTasksFragment) currentFragment).notifyDataChanged();
                    }

                    smallTasksViewPager.setCurrentItem(position);

                    setTitle(FolderRealmController.getFolder(idFolderFromTag).getName());
                    slidingUpPanelLayout. setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            };

        onHolderTextViewOnLongClickListener
            = (FolderOfTaskRecyclerViewAdapter.ViewHolder holder, int position) -> {
            idFolderFromTag = (Long) holder.itemView.findViewById(R.id.item_text).getTag();
            titleFolder = FolderRealmController.getFolder(idFolderFromTag).getName();
            finishActionMode();
            actionMode = getActivity().startActionMode(getCallback(FOLDER_ACTIONMODE));

            /*
            todo 1 need delete smalltaskfragment after delete list and need reset actionmode
            todo 2 and запрет открытия слайдин панели если нулл или может быть удален лист
            */
        };

        onFooterTextViewOnClickListener
            = (FolderOfTaskRecyclerViewAdapter.ViewHolder holder, int position) -> {
            AddFolderDialog addFolderDialog = new AddFolderDialog();
            addFolderDialog.show(getActivity().getSupportFragmentManager(), "addfolder");
        };

        folderOfTaskRVAdapter.setOnHolderTextViewOnClickListener(onHolderTextViewOnClickListener);
        folderOfTaskRVAdapter.setOnHolderTextViewOnLongClickListener(onHolderTextViewOnLongClickListener);
        folderOfTaskRVAdapter.setOnFooterTextViewSetOnClickListener(onFooterTextViewOnClickListener);

        // set ITEM TOUCH HELPER for folder rv
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP | ItemTouchHelper.DOWN ,0) {

                int dragFrom = -1;
                int dragTo = -1;

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                      RecyclerView.ViewHolder target) {

                    int fromPosition = viewHolder.getAdapterPosition();
                    int toPosition = target.getAdapterPosition();

                    if (dragFrom == -1) {
                        dragFrom = fromPosition;
                    }
                    dragTo = toPosition;

                    folderOfTaskRVAdapter.notifyItemMoved(fromPosition, toPosition);
                    return true;
                }

//            private void reallyMoved(int from, int to) {arrayList.add(to, arrayList.remove(from));}

                @Override
                public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    super.clearView(recyclerView, viewHolder);

                    if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
//                    reallyMoved(dragFrom, dragTo);
//                    if (!arrayList.equals(arrayListCopy)){
//                        arrayListCopy=arrayList.clone();
//                    }
                    }
                    dragFrom = dragTo = -1;
                }

                @Override
                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                }
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
            public void onPageSelected(int position) { finishActionMode(); }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        setDayScopeValue();
        menu.clear();
        MenuItem dayScopeMenu = menu.add(100,100,100,"" + dayScope);
        dayScopeMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        dayScopeMenu.setOnMenuItemClickListener((v)->{dayScope=+1; return true;});
        super.onCreateOptionsMenu(menu, inflater);
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
    }

//    protected void tasksDataChanged(){
//        setTasks();
//    }


    public void finishActionMode(){
        ((MainActivity) getActivity()).startSupportActionMode(new EmptyActionModeCallback());
    }

    private void setDayScopeValue(){
        // done and not done tasks but where countAccumulation more than 0
        List<TaskObject> allDoneAndParticullaryDoneTasks = TasksRealmController.getDoneAndPartiallyDoneTasks();

        int todayDate = Integer.valueOf("" + Calendar.getInstance().get(Calendar.DAY_OF_YEAR) +
                Calendar.getInstance().get(Calendar.YEAR));

        dayScope = 0;

        for (TaskObject task : allDoneAndParticullaryDoneTasks) {
            if (task.getLastDoneDate() == todayDate) {
                int equalDateCount = 0;
                for (RealmInteger realmInteger : task.getDateCountAccumulation()) {
                    if (realmInteger.getMyInteger() == todayDate) {
                        equalDateCount++;
                    }
                }
                dayScope = dayScope + task.getCountValue() * equalDateCount;
            }
        }
    }

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


// old unused method    , todo thinc about logic открытия таск панели если нет фолдеров или какой по умоллчанию откроется (видимо откроется первый и использовать эмпти стэйт или запретить экспандить панель)
//    public void setTasks(){
//            if (tasksListId == 0) return;
//            else{
//                tasks = TasksRealmController.getNotDoneTasks(tasksListId);
//                doneTasks = TasksRealmController.getDoneTasks(tasksListId);
//                ((MainActivity) getActivity()).getSupportActionBar()
//                        .setTitle((CharSequence) FolderRealmController.getFolder(tasksListId).getName());
//            }
//            adapter = new TasksRecyclerViewAdapter(tasks, doneTasks, this);
//            rvItems.setAdapter(adapter);
//            adapter.setOnLongClicked(onItemLongClicked);
//            adapter.setOnClicked(onItemClicked);
//    }
}







