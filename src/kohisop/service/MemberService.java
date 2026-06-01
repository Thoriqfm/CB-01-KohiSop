package kohisop.service;

import kohisop.model.membership.Member;
import java.util.HashMap;
import java.util.Map;

/**
 * Service untuk menangani logika bisnis member
 */
public class MemberService {
    
    private Map<String, Member> memberDatabase = new HashMap<>();
    
    /**
     * Registrasi member baru
     */
    public Member registrasiMember(String kodeMember, String nama) throws IllegalArgumentException {
        if (kodeMember == null || kodeMember.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode member tidak boleh kosong!");
        }
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama member tidak boleh kosong!");
        }
        if (memberDatabase.containsKey(kodeMember)) {
            throw new IllegalArgumentException("Kode member sudah terdaftar!");
        }
        
        Member member = new Member(kodeMember, nama);
        memberDatabase.put(kodeMember, member);
        return member;
    }
    
    /**
     * Cari member berdasarkan kode member
     */
    public Member cariMember(String kodeMember) throws IllegalArgumentException {
        if (kodeMember == null || kodeMember.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode member tidak boleh kosong!");
        }
        Member member = memberDatabase.get(kodeMember);
        if (member == null) {
            throw new IllegalArgumentException("Member dengan kode " + kodeMember + " tidak ditemukan!");
        }
        return member;
    }
    
    /**
     * Validasi member bukan null
     */
    public void validasiMemberExists(Member member) throws IllegalArgumentException {
        if (member == null) {
            throw new IllegalArgumentException("Member tidak valid!");
        }
    }
    
    /**
     * Tambah poin member
     */
    public void tambahPoin(Member member, double jumlahBelanja) {
        if (member != null) {
            int poinDidapat = (int) (jumlahBelanja / 10);
            if (member.isPoinGanda()) {
                poinDidapat *= 2;
            }
            member.tambahPoin(jumlahBelanja);
        }
    }
    
    /**
     * Gunakan poin member
     */
    public void gunakanPoin(Member member, double jumlahPoin) throws IllegalArgumentException {
        if (member == null) {
            throw new IllegalArgumentException("Member tidak valid!");
        }
        if (member.getPoin() < jumlahPoin) {
            throw new IllegalArgumentException("Poin tidak cukup!");
        }
        member.gunakanPoin(jumlahPoin);
    }
    
    /**
     * Get all members
     */
    public Map<String, Member> semuaMember() {
        return new HashMap<>(memberDatabase);
    }
    
    /**
     * Clear all members (untuk testing)
     */
    public void clearAll() {
        memberDatabase.clear();
    }
}
