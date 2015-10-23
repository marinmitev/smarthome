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
     * The template is visible by everyone.
     */
    PUBLIC,

    /**
     * The template is visible only by its creator.
     */
    PRIVATE
}
