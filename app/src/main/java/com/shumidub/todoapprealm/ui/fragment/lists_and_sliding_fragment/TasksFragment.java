package com.shumidub.todoapprealm.ui.fragment.lists_and_sliding_fragment;


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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.realmmodel.FolderObject;
import com.shumidub.todoapprealm.realmmodel.RealmInteger;
import com.shumidub.todoapprealm.realmmodel.TaskObject;
import com.shumidub.todoapprealm.ui.fragment.small_tasks_fragment.SmallTasksFragment;

import com.shumidub.todoapprealm.realmcontrollers.FolderRealmController;
import com.shumidub.todoapprealm.realmcontrollers.TasksRealmController;
import com.shumidub.todoapprealm.ui.activity.mainactivity.MainActivity;
import com.shumidub.todoapprealm.ui.actionmode.ActionModeListCallback;
import com.shumidub.todoapprealm.ui.fragment.small_tasks_fragment.SmallTaskFragmentPagerAdapter;
import com.shumidub.todoapprealm.ui.dialog.DialogAddList;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Calendar;
import java.util.List;


import io.realm.RealmResults;

import static com.shumidub.todoapprealm.realmcontrollers.FolderRealmController.listOfFolderIsEmpty;

/**
 * Created by Артем on 19.12.2017.
 */

public class TasksFragment extends Fragment {

    //MAIN
    ActionBar actionBar;
    public SlidingUpPanelLayout slidingUpPanelLayout;
    LinearLayout llBottomFooter;
    public int dayScope;

    //TASKS
    ViewPager smallTasksViewPager;
    long tasksListId;

    //FOLDERS VIEW

    EditText et;
    TextView tvTaskCountValue;
    TextView tvTaskMaxAccumulate;
    TextView tvTaskPriority;
    TextView tvTaskCycling;


    TasksListRecyclerViewAdapter.OnHolderTextViewOnClickListener onHolderTextViewOnClickListener;
    TasksListRecyclerViewAdapter.OnHolderTextViewOnLongClickListener onHolderTextViewOnLongClickListener;
    TasksListRecyclerViewAdapter.OnFooterTextViewOnClickListener onFooterTextViewOnClickListener;

    SmallTaskFragmentPagerAdapter smallTaskFragmentPagerAdapter;

    TasksListRecyclerViewAdapter tasksListRecyclerViewAdapter;
//    ExpandableListView expandableListView;

    RecyclerView rvLists;

    AdapterView.OnItemLongClickListener longListener;
    ExpandableListView.OnChildClickListener childClickListener;

    static ActionMode actionMode;

    ActionMode.Callback listCallback;

    //FOLDERS (LISTS)
    RealmResults<FolderObject> lists;


    public static String titleList;
    public static String listName;
    public static long listId;


    static final int LIST_ACTIONMODE = 2;

    public static Long idOnTag;

    private static String title;


    int priority = 0;
    boolean cycling = false;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resetTasksCountAccumulationAndSetDayScopeValue(true,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.item_fragment_layout, container, false);
        View view = inflater.inflate(R.layout.task_slideup_panel_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);

        llBottomFooter = view.findViewById(R.id.ll_footer);

        slidingUpPanelLayout = view.findViewById(R.id.slidingup_panel_layout);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        llBottomFooter.setVisibility(View.VISIBLE);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        setTitle("Tasks");

        //todo realise
        slidingUpPanelLayout.setStateListAnimator(new StateListAnimator());
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                llBottomFooter.setAlpha(1.0f - slideOffset);
                if (slideOffset > 0.87){
                    llBottomFooter.setVisibility(View.GONE);
                } else if (slideOffset<0.85){
                    llBottomFooter.setVisibility(View.VISIBLE);
                }


                if (slideOffset > 0.3 && slideOffset < 0.7){
                    if (actionMode!=null){
                        actionMode.finish();
                    }


                    Fragment currentFragment = smallTaskFragmentPagerAdapter.getItem(smallTasksViewPager.getCurrentItem());
                    if (currentFragment instanceof SmallTasksFragment){
                        ((SmallTasksFragment) currentFragment).finishActionMode();
                    }


                }




            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) setTitle("Tasks");
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
//                    setTitle(FolderRealmController.getFolder(tasksListId).getName());
                    if (actionMode!=null) actionMode.finish();
                }
            }
        });

        //FOLDER
        findFolderViews(view);
        rvLists.setLayoutManager(new LinearLayoutManager(getContext()));
        lists = FolderRealmController.getFolders();
        tasksListRecyclerViewAdapter = new TasksListRecyclerViewAdapter(lists, getActivity());


        onHolderTextViewOnClickListener =
                new TasksListRecyclerViewAdapter.OnHolderTextViewOnClickListener() {
                    @Override
                    public void onClick(TasksListRecyclerViewAdapter.ViewHolder holder, int position) {

                        if (actionMode!=null) actionMode.finish();

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
                            tasksListId = (Long) holder.itemView.findViewById(R.id.item_text).findViewById(R.id.item_text).getTag();

                            // setTasks();

                            Fragment currentFragment = smallTaskFragmentPagerAdapter.getItem(position);
                            if (currentFragment instanceof SmallTasksFragment){
                                ((SmallTasksFragment) currentFragment).notifyDataChanged();
                            }

                            smallTasksViewPager.setCurrentItem(position);

                            setTitle(FolderRealmController.getFolder(tasksListId).getName());
                            slidingUpPanelLayout. setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        }
                    }
                };


        onHolderTextViewOnLongClickListener = new TasksListRecyclerViewAdapter.OnHolderTextViewOnLongClickListener() {
            @Override
            public void onLongClick(TasksListRecyclerViewAdapter.ViewHolder holder, int position) {


                idOnTag = (Long) holder.itemView.findViewById(R.id.item_text).getTag();
                titleList = FolderRealmController.getFolder(idOnTag).getName();

                if (actionMode!=null) actionMode.invalidate();

                actionMode = getActivity().startActionMode(getCallback(LIST_ACTIONMODE));

                /*
                todo need delete smalltaskfragment after delete list and need reset actionmode
                 and запрет открытия слайдин панели если нулл или может быть удален лист
                */
            }
        };

        onFooterTextViewOnClickListener = new TasksListRecyclerViewAdapter.OnFooterTextViewOnClickListener() {
            @Override
            public void onClick(TasksListRecyclerViewAdapter.ViewHolder holder, int position) {
                DialogAddList dialogAddList = new DialogAddList();
                dialogAddList.show(getActivity().getSupportFragmentManager(), "addtocategory");
            }
        };

        tasksListRecyclerViewAdapter.setOnHolderTextViewOnClickListener(onHolderTextViewOnClickListener);
        tasksListRecyclerViewAdapter.setOnHolderTextViewOnLongClickListener(onHolderTextViewOnLongClickListener);
        tasksListRecyclerViewAdapter.setOnFooterTextViewSetOnClickListener(onFooterTextViewOnClickListener);


        rvLists.setAdapter(tasksListRecyclerViewAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(rvLists);



//        expandableListView.setAdapter(new CategoriesAndListsAdapter(getContext()).getAdapter());
//        expandableListView.setOnItemLongClickListener(getLongListener());
//        expandableListView.setOnChildClickListener(getChildClickListener());

        setEmptyStateIfListsIsEmpty(view);



        //SmallTAsks
        smallTasksViewPager = view.findViewById(R.id.view_pager_small_tasks);
        smallTaskFragmentPagerAdapter = new SmallTaskFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        smallTasksViewPager.setAdapter(smallTaskFragmentPagerAdapter);


        smallTasksViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Fragment currentFragment = smallTaskFragmentPagerAdapter.getItem(position);
                if (currentFragment instanceof SmallTasksFragment){
                    ((SmallTasksFragment) currentFragment).finishActionMode();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        resetTasksCountAccumulationAndSetDayScopeValue(false, true);

        menu.clear();

        MenuItem dayScopeMenu = menu.add(100,100,100,"" + dayScope);

        dayScopeMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        dayScopeMenu.setOnMenuItemClickListener((v)->{dayScope=+1; return true;});


        super.onCreateOptionsMenu(menu, inflater);
    }

    //FOLDER
    private void findFolderViews(View view){
        rvLists = view.findViewById(R.id.rv_lists);
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

    private void setEmptyStateIfListsIsEmpty(View view){
        if (listOfFolderIsEmpty()){
            (view.findViewById(R.id.tv_empty)).setVisibility(View.VISIBLE);
        } else (view.findViewById(R.id.tv_empty)).setVisibility(View.INVISIBLE);
    }

//    private AdapterView.OnItemLongClickListener getLongListener(){
//        if (longListener == null) {
//            longListener = (adapterView, view, i,l) -> {
//
//                String type = ((Pair<String, Long>) view.getTag()).first;
//                idOnTag = ((Pair<String, Long>) view.getTag()).second;
//                String subtitle = "";
//
//                if (view.getId() == R.id.parent_text1) {
//                    try {
//                        CategoryModel categoty = CategoriesRealmController.getCategory(idOnTag);
//                        textCategoryName =categoty.getName();
//                        subtitle = "Category";
//                        actionMode = getActivity().startActionMode(getCallback(CATEGORY_ACTIONMODE));
//                    } catch (IndexOutOfBoundsException ignored) { }
//                    Log.d("DEBUG_TAG", "onItemLongClick: parent  index out");
//                }else if (view.getId() == R.id.child_text1) {
//                    try {
//                        titleList = FolderRealmController.getFolder(idOnTag).getName();
//                        actionMode = null;
//                        subtitle = "List";
//                        actionMode = getActivity().startActionMode(getCallback(LIST_ACTIONMODE));
//                    } catch (IndexOutOfBoundsException ignored) { }
//                }
//                actionMode.setSubtitle(subtitle);
//                return true;
//            };
//        }
//        return longListener;
//    }

    private ActionMode.Callback getCallback(int callbackType){

        if (callbackType == LIST_ACTIONMODE) {
            listCallback = new ActionModeListCallback().getListActionModeCallback((MainActivity) getActivity(), this, idOnTag);
            return listCallback;
        }
        else return null;
    }

//    protected ExpandableListView.OnChildClickListener getChildClickListener(){
//        if (childClickListener == null) {
//            childClickListener = new ExpandableListView.OnChildClickListener() {
//                @Override
//                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
//                    String text = et.getText().toString();
//                    int count = Integer.valueOf(tvTaskCountValue.getText().toString());
//                    int maxAccumulation = Integer.valueOf(tvTaskMaxAccumulate.getText().toString());
//
//                    if (!text.isEmpty() || !text.equals("")) {
//                        TasksRealmController.addTask(text, count , maxAccumulation, cycling, priority,
//                                ((Pair<String, Long>) view.getTag()).second );
//                        priority = 0;
//                        cycling = false;
//                        et.setText("");
//                    } else {
//                        tasksListId = ((Pair<String, Long>) view.getTag()).second;
//                        //todo realise expandable replace recycler + hasFixedSize + getPosition + onclick set smallFragmentPosition(recyclerView item position )
//                        //todo set right title
////                        tasksDataChanged();
//                        slidingUpPanelLayout. setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                        setTitle(FolderRealmController.getFolder(tasksListId).getName());
//                    }
//                    return false;
//                }
//            };
//        }
//        return childClickListener;
//    }

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

//      lists = FolderRealmController.getFolders();
//      tasksListRecyclerViewAdapter.notifyDataSetChanged();

        tasksListRecyclerViewAdapter.notifyDataSetChanged();
        smallTaskFragmentPagerAdapter.notifyDataSetChanged();

    }

//    protected void tasksDataChanged(){
//        setTasks();
//    }

    public static void finishActionMode(){
        if (actionMode!=null) actionMode.finish();
    }


    private void  resetTasksCountAccumulationAndSetDayScopeValue(boolean resetTasksCountAccumulation,
                                                                 boolean setDayScopeValue){
        // done and not done tasks but where countAccumulation more than 0
        List<TaskObject> allDoneAndParticullaryDoneTasks = TasksRealmController.getDoneAndPartiallyDoneTasks();

        //TASK
        dayScope = 0;
        int date = Integer.valueOf("" + Calendar.getInstance().get(Calendar.DAY_OF_YEAR) +
                Calendar.getInstance().get(Calendar.YEAR));



        for (TaskObject task : allDoneAndParticullaryDoneTasks) {



            if (resetTasksCountAccumulation){
                if (task.isCycling()  && task.getLastDoneDate() != date ){
                    TasksRealmController.setTaskDoneOrParticullaryDone(task, false);
                }
            }




            if (setDayScopeValue){
                if (task.getLastDoneDate() == date) {
                    int equalDateCount = 0;

                    for (RealmInteger realmInteger : task.getDateCountAccumulation()) {
                        if (realmInteger.getMyInteger() == date) {
                            equalDateCount++;
                        }
                    }

                    dayScope = dayScope + task.getCountValue() * equalDateCount;
                }
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


    @SuppressWarnings("all")
    private ItemTouchHelper.Callback createHelperCallback() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN ,
                0) {

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

                tasksListRecyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);
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
        };



        return simpleItemTouchCallback;
    }

}







