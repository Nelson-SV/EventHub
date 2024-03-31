package view.admin.mainAdmin;


import bll.EventManagementLogic;
import bll.ILogicManager;
import exceptions.EventException;

/**
 * We will use dependency injection instead off the singleton design pattern,
 * this will allow us to  test the controllers in isolation*/
public class AdminModel {
    private ILogicManager adminLogic;

    public AdminModel() throws EventException {
        this.adminLogic = new EventManagementLogic();
    }
}
