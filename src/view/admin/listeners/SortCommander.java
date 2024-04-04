package view.admin.listeners;

import be.Status;

public interface SortCommander {
   void  performSortOperation(Status status);
   void setLatestSelected(String id);
}
