package com.booksphere.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclass: Member (extends User)
 * 
 * Demonstrates:
 * 1. Inheritance (extends User)
 * 2. Collections (ArrayList of borrowed item IDs)
 * 3. Encapsulation with business checks
 */
public class Member extends User {
    private String membershipCardNumber;
    private int maxBorrowLimit;
    private List<Integer> borrowedBookIds;

    public Member() {
        super();
        this.maxBorrowLimit = 5;
        this.borrowedBookIds = new ArrayList<>();
    }

    public Member(int userId, String username, String fullName, String email, String membershipCardNumber, int maxBorrowLimit) {
        super(userId, username, fullName, email, "MEMBER");
        this.membershipCardNumber = membershipCardNumber;
        this.maxBorrowLimit = maxBorrowLimit;
        this.borrowedBookIds = new ArrayList<>();
    }

    public boolean canBorrowMore() {
        return borrowedBookIds.size() < maxBorrowLimit;
    }

    public void addBorrowedBook(int bookId) {
        if (!borrowedBookIds.contains(bookId)) {
            borrowedBookIds.add(bookId);
        }
    }

    public void removeBorrowedBook(int bookId) {
        borrowedBookIds.remove(Integer.valueOf(bookId));
    }

    public String getMembershipCardNumber() {
        return membershipCardNumber;
    }

    public void setMembershipCardNumber(String membershipCardNumber) {
        this.membershipCardNumber = membershipCardNumber;
    }

    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }

    public void setMaxBorrowLimit(int maxBorrowLimit) {
        this.maxBorrowLimit = maxBorrowLimit;
    }

    public List<Integer> getBorrowedBookIds() {
        return borrowedBookIds;
    }

    public void setBorrowedBookIds(List<Integer> borrowedBookIds) {
        this.borrowedBookIds = borrowedBookIds;
    }
}
