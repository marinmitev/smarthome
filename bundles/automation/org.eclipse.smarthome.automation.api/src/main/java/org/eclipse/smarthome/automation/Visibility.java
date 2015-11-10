package org.eclipse.smarthome.automation;

import org.eclipse.smarthome.automation.template.Template;
import org.eclipse.smarthome.automation.type.ModuleType;

/**
 * Defines visibility values of {@link ModuleType}s and {@link Template}s
 *
 * @author Yordan Mihaylov - Initial Contribution
 *
 */
public enum Visibility {
    /**
     * The UI has to show this object.
     */
    VISIBLE,

    /**
     * The UI has to hide this object.
     */
    HIDDEN,

    /**
     * The UI has to show this object only to experts.
     */
    EXPERT

}