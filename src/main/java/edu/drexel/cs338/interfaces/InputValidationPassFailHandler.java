package edu.drexel.cs338.interfaces;

import javax.swing.*;

/*
 * Angel Delgado
 * ald363@drexel.edu
 * CS338:GUI, Assignment 2, Trip Tracker
 */

/**
 * This interface is used to allow the presenter handle validation
 */
public interface InputValidationPassFailHandler {
    void fail(String message, JComponent component);
    void pass(JComponent component);
}
