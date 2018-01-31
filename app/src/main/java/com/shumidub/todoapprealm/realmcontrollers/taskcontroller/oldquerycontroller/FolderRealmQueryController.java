//package com.shumidub.todoapprealm.realmcontrollers;
//
//import com.shumidub.todoapprealm.App;
//import com.shumidub.todoapprealm.realmmodel.FolderObject;
//import com.shumidub.todoapprealm.realmmodel.RealmFoldersContainer;
//
//import io.realm.RealmList;
//import io.realm.RealmQuery;
//import io.realm.RealmResults;
//import static com.shumidub.todoapprealm.App.realm;
//
///**
// * Created by Артем on 24.12.2017.
// */
//
//public class FolderRealmQueryController {
//
//    private static RealmQuery<FolderObject> foldersQuery;
//    private static RealmResults<FolderObject> folders;
//
//    /** Get all folder */
//    public static RealmResults<FolderObject> getFolders(){
//        App.initRealm();
//        folders = getFoldersQuery().findAll();
//        return folders;
//    }
//
//    /** get folder by id */
//    public static FolderObject getFolder(long listId){
//        return getFoldersQuery().equalTo("id", listId).findFirst();
//    }
//
//    /** add folder */
//    public static long addFolder(String name){
//        long id = getIdForNextValue();
//        App.initRealm();
//        App.realm.executeTransaction((transaction) -> {
//                FolderObject folder = App.realm.createObject(FolderObject.class);
//                folder.setId(id);
//                folder.setName(name);
//
//                App.realm.insert(folder);
//        });
//        return id;
//    }
//
//    /** edit folder by folderobject */
//    public static long editFolder(FolderObject folder, String name){
//        App.initRealm();
//        realm.executeTransaction((transaction)-> folder.setName(name));
//        return folder.getId();
//    }
//
//    /** edit folder by id */
//    public static long editFolder(long id, String name){
//        App.initRealm();
//        FolderObject folder = getFolder(id);
//        return editFolder(folder, name);
//    }
//
//    /** delete folder by folderobject */
//    public static void deleteFolder(FolderObject list){
//        App.initRealm();
//        realm.executeTransaction((transaction)-> list.deleteFromRealm());
//    }
//
//    /** delete folder by id */
//    public static void deleteFolder(long idList){
//        App.initRealm();
//        FolderObject list = getFoldersQuery().equalTo("id", idList).findFirst();
//        deleteFolder(list);
//    }
//
//    /** folder is valid */
//    public static boolean folderIsExist(FolderObject list){
//        App.initRealm();
//        return list.isValid();
//    }
//
//    /** folder is exist and valid */
//    public static boolean folderIsExist(long idList){
//        App.initRealm();
//        if ( realm.where(FolderObject.class).equalTo("id", idList).findFirst() == null){
//            return false;
//        }else {
//            return realm.where(FolderObject.class).equalTo("id", idList).findFirst().isValid();
//        }
//    }
//
//    /** folders is not exist, haven,t any folder*/
//    public static boolean listOfFolderIsEmpty(){
//        App.initRealm();
//        return (realm.where(FolderObject.class).findAll() == null
//                || realm.where(FolderObject.class).findAll().size() == 0);
//    }
//
//    /** Промежуточный запрос */
//    private static RealmQuery<FolderObject> getFoldersQuery(){
//        App.initRealm();
//        foldersQuery = realm.where(FolderObject.class);
//        return foldersQuery;
//    }
//
//    /** get unique id*/
//    static long getIdForNextValue() {
//        long id =  System.currentTimeMillis();
//        while ((App.realm.where(FolderObject.class).equalTo("id", id)).findFirst()!=null){
//            id ++;
//        }
//        return id;
//    }
//}
