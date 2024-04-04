package view.admin.listeners;



public interface SortObserver {
   void addSubject(SortSubject subject);
   void  removeSubject(SortSubject subject);
   void  notifySubjects();
}
