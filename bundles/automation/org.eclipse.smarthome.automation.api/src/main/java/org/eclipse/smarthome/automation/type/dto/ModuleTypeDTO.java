/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.type.dto;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.smarthome.automation.type.ActionType;
import org.eclipse.smarthome.automation.type.CompositeActionType;
import org.eclipse.smarthome.automation.type.CompositeConditionType;
import org.eclipse.smarthome.automation.type.CompositeTriggerType;
import org.eclipse.smarthome.automation.type.ConditionType;
import org.eclipse.smarthome.automation.type.ModuleType;
import org.eclipse.smarthome.automation.type.TriggerType;

/**
 * This class is responsible for custom serialization and deserialization of the {@link ModuleType}s. It is necessary
 * for the persistence of the {@link ModuleType}s.
 *
 * @param <T> is one of {@link TriggerType}, {@link CompositeTriggerType}, {@link ConditionType},
 *            {@link CompositeConditionType}, {@link ActionType} or {@link CompositeActionType}.
 *
 * @author Ana Dimova - Initial Contribution
 * @author Kai Kreuzer - changed naming to DTO convention
 */
public final class ModuleTypeDTO {

    public String vendorId;
    public String vendorVersion;
    public String url;
    public int type;
    public Set<ActionType> localizedActionTypes = new HashSet<ActionType>();
    public Set<CompositeActionTypeDTO> localizedCActionTypes = new HashSet<CompositeActionTypeDTO>();
    public Set<ConditionType> localizedConditionTypes = new HashSet<ConditionType>();
    public Set<CompositeConditionTypeDTO> localizedCConditionTypes = new HashSet<CompositeConditionTypeDTO>();
    public Set<TriggerType> localizedTriggerTypes = new HashSet<TriggerType>();
    public Set<CompositeTriggerTypeDTO> localizedCTriggerTypes = new HashSet<CompositeTriggerTypeDTO>();
    public Set<String> languages;

    /**
     * This constructor is used for deserialization of the {@link ModuleType}s.
     *
     */
    public ModuleTypeDTO() {}

}