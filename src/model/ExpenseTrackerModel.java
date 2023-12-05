package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The ExpenseTrackerModel class represents the model part of the MVC pattern
 * for an expense tracker application.
 * It manages the transactions and notifies registered listeners about state
 * changes.
 */
public class ExpenseTrackerModel {

  // encapsulation - data integrity
  private List<Transaction> transactions;
  private List<Integer> matchedFilterIndices;
  private Set<ExpenseTrackerModelListener> listeners = new HashSet<>();

  // This is applying the Observer design pattern.
  // Specifically, this is the Observable class.
  /**
   * Constructor for ExpenseTrackerModel.
   * Initializes the list of transactions and matched filter indices.
   */
  public ExpenseTrackerModel() {
    transactions = new ArrayList<Transaction>();
    matchedFilterIndices = new ArrayList<Integer>();
  }

  /**
   * Adds a transaction to the expense tracker.
   *
   * @param t The Transaction to be added.
   * @throws IllegalArgumentException if the transaction is null.
   */
  public void addTransaction(Transaction t) {
    // Perform input validation to guarantee that all transactions added are
    // non-null.
    if (t == null) {
      throw new IllegalArgumentException("The new transaction must be non-null.");
    }
    transactions.add(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    stateChanged();
  }

  /**
   * Removes a transaction from the expense tracker.
   *
   * @param t The Transaction to be removed.
   */
  public void removeTransaction(Transaction t) {
    transactions.remove(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    stateChanged();
  }

  /**
   * Retrieves an unmodifiable list of transactions.
   *
   * @return An unmodifiable list of transactions.
   */
  public List<Transaction> getTransactions() {
    // encapsulation - data integrity
    return Collections.unmodifiableList(new ArrayList<>(transactions));
  }

  /**
   * Sets the list of indices that match a certain filter.
   *
   * @param newMatchedFilterIndices A List of integers representing matched filter
   *                                indices.
   * @throws IllegalArgumentException if the list is null or contains invalid
   *                                  indices.
   */
  public void setMatchedFilterIndices(List<Integer> newMatchedFilterIndices) {
    // Perform input validation
    if (newMatchedFilterIndices == null) {
      throw new IllegalArgumentException("The matched filter indices list must be non-null.");
    }
    for (Integer matchedFilterIndex : newMatchedFilterIndices) {
      if ((matchedFilterIndex < 0) || (matchedFilterIndex > this.transactions.size() - 1)) {
        throw new IllegalArgumentException(
            "Each matched filter index must be between 0 (inclusive) and the number of transactions (exclusive).");
      }
    }
    // For encapsulation, copy in the input list
    this.matchedFilterIndices.clear();
    this.matchedFilterIndices.addAll(newMatchedFilterIndices);
    stateChanged();
  }

  /**
   * Retrieves a copy of the list of matched filter indices.
   *
   * @return A list of integers representing matched filter indices.
   */
  public List<Integer> getMatchedFilterIndices() {
    // For encapsulation, copy out the output list
    List<Integer> copyOfMatchedFilterIndices = new ArrayList<Integer>();
    copyOfMatchedFilterIndices.addAll(this.matchedFilterIndices);
    return copyOfMatchedFilterIndices;
  }

  /**
   * Registers the given ExpenseTrackerModelListener for
   * state change events.
   *
   * @param listener The ExpenseTrackerModelListener to be registered
   * @return If the listener is non-null and not already registered,
   *         returns true. If not, returns false.
   */
  public boolean register(ExpenseTrackerModelListener listener) {
    // For the Observable class, this is one of the methods.
    //
    if (listener == null || listeners.contains(listener)) {
      return false;
    }
    listeners.add(listener);
    return true;
  }

  /**
   * Returns the number of registered listeners.
   *
   * @return The number of listeners.
   */
  public int numberOfListeners() {
    // For testing, this is one of the methods.
    //
    return listeners.size();
  }

  /**
   * Checks if a listener is already registered.
   *
   * @param listener The listener to check.
   * @return true if the listener is registered, otherwise false.
   */
  public boolean containsListener(ExpenseTrackerModelListener listener) {
    // For testing, this is one of the methods.
    //
    return listeners.contains(listener);
  }

  /**
   * Notifies all registered listeners about a state change.
   */
  protected void stateChanged() {
    // For the Observable class, this is one of the methods.
    //
    for (ExpenseTrackerModelListener listener : listeners) {
      listener.update(this);
    }
  }
}
