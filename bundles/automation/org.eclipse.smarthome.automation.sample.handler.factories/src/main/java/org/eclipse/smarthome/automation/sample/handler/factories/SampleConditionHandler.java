/*******************************************************************************
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH
 * http://www.prosyst.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    ProSyst Software GmbH - initial API and implementation
 *******************************************************************************/

package org.eclipse.smarthome.automation.sample.handler.factories;

import java.util.Map;

import org.eclipse.smarthome.automation.Condition;
import org.eclipse.smarthome.automation.handler.ConditionHandler;
import org.eclipse.smarthome.automation.type.ConditionType;
import org.osgi.framework.BundleContext;

/**
 * Condition Handler sample implementation
 *
 * @author Vasil Ilchev - Initial Contribution
 */
public class SampleConditionHandler implements ConditionHandler {
    public static final String OPERATOR_LESS = "<";
    public static final String OPERATOR_GREATER = ">";
    public static final String OPERATOR_EQUAL = "=";
    public static final String OPERATOR_NOT_EQUAL = "!=";
    //
    public static final String PROPERTY_OPERATOR = "operator";
    public static final String PROPERTY_CONSTRAINT = "constraint";
    public static final String CONDITION_INPUT_NAME = "conditionInput";
    //
    private Map<String, ?> configuration;
    private Condition condition;
    private ConditionType conditionType;
    //
    private BundleContext bc;
    private SampleHandlerFactory handlerFactory;

    public SampleConditionHandler(SampleHandlerFactory handlerFactory, Condition condition,
            ConditionType conditionType, BundleContext bc) {
        this.condition = condition;
        this.conditionType = conditionType;
        this.configuration = condition.getConfiguration();
        this.handlerFactory = handlerFactory;
    }

    @Override
    public void setConfiguration(Map<String, ?> configuration) {
        this.configuration = configuration;
    }

    @Override
    public void dispose() {
        handlerFactory.disposeHandler(this);
    }

    @Override
    public boolean isSatisfied(Map<String, ?> inputs) {
        String conditionInput = (String) inputs.get(CONDITION_INPUT_NAME);
        String operator = (String) configuration.get(PROPERTY_OPERATOR);
        String constraint = (String) configuration.get(PROPERTY_CONSTRAINT);
        boolean satisfied = false;
        if (OPERATOR_EQUAL.equals(operator)) {
            satisfied = conditionInput.equals(constraint);
        } else if (OPERATOR_NOT_EQUAL.equals(operator)) {
            satisfied = !(conditionInput.equals(constraint));
        } else if (OPERATOR_LESS.equals(operator)) {
            int compersion = conditionInput.compareTo(constraint);
            satisfied = compersion < 0 ? true : false;
        } else if (OPERATOR_GREATER.equals(operator)) {
            int comperison = conditionInput.compareTo(constraint);
            satisfied = comperison > 0 ? true : false;
        }
        return satisfied;
    }

}
