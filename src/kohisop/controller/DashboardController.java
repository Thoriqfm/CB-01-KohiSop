package kohisop.controller;

import kohisop.model.entities.MenuItem;
import kohisop.service.MenuService;
import java.util.ArrayList;

/**
 * Controller untuk menangani event dashboard dari View
 */
public class DashboardController {
    
    private MenuService menuService;
    
    public DashboardController() {
        this.menuService = new MenuService();
    }
    
    /**
     * Get semua menu makanan
     */
    public ArrayList<MenuItem> getSemuaMakanan() {
        return menuService.getDaftarMakanan();
    }
    
    /**
     * Get semua menu minuman
     */
    public ArrayList<MenuItem> getSemuaMinuman() {
        return menuService.getDaftarMinuman();
    }
    
    /**
     * Get menu service untuk akses langsung (jika diperlukan)
     */
    public MenuService getMenuService() {
        return menuService;
    }
}
