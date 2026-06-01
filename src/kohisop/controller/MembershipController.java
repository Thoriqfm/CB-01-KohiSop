package kohisop.controller;

import kohisop.model.membership.Member;
import kohisop.model.entities.ItemPesanan;
import kohisop.service.PesananService;
import kohisop.service.MemberService;

import java.util.ArrayList;

/**
 * Controller untuk menangani event membership dari View
 */
public class MembershipController {
    
    private MemberService memberService;
    private PesananService pesananService;
    
    // Current state
    private Member currentMember;
    private ArrayList<ItemPesanan> pesananItems;
    
    public MembershipController() {
        this.memberService = new MemberService();
        this.pesananService = new PesananService();
        this.pesananItems = new ArrayList<>();
    }
    
    /**
     * Cari atau buat member baru
     */
    public Member getAtauBuatMember(String kodeMember, String nama) {
        try {
            currentMember = memberService.cariMember(kodeMember);
            return currentMember;
        } catch (IllegalArgumentException e) {
            // Member tidak ditemukan, buat baru
            return memberService.registrasiMember(kodeMember, nama);
        }
    }
    
    /**
     * Hitung total belanja
     */
    public double hitungTotalBelanja() {
        return pesananService.hitungTotalBelanja(pesananItems);
    }
    
    /**
     * Add item ke pesanan
     */
    public void tambahItemPesanan(ItemPesanan item) {
        if (item != null) {
            pesananItems.add(item);
        }
    }
    
    /**
     * Update item pesanan
     */
    public void updateItemPesanan(int index, int quantity) throws IllegalArgumentException {
        if (index < 0 || index >= pesananItems.size()) {
            throw new IllegalArgumentException("Item tidak ditemukan!");
        }
        if (quantity == 0) {
            pesananItems.remove(index);
        } else {
            pesananService.updateQuantity(pesananItems.get(index), quantity);
        }
    }
    
    /**
     * Get current pesanan items
     */
    public ArrayList<ItemPesanan> getPesananItems() {
        return new ArrayList<>(pesananItems);
    }
    
    /**
     * Clear pesanan
     */
    public void clearPesanan() {
        pesananItems.clear();
    }
    
    /**
     * Get current member
     */
    public Member getCurrentMember() {
        return currentMember;
    }
    
    /**
     * Set current member
     */
    public void setCurrentMember(Member member) {
        this.currentMember = member;
    }
}
