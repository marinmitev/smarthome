/**
 * Copyright (c) 1997, 2015 by ProSyst Software GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.core.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.eclipse.smarthome.automation.core.RuleEngine;
import org.eclipse.smarthome.automation.type.ActionType;
import org.eclipse.smarthome.automation.type.CompositeActionType;
import org.eclipse.smarthome.automation.type.CompositeConditionType;
import org.eclipse.smarthome.automation.type.CompositeTriggerType;
import org.eclipse.smarthome.automation.type.ConditionType;
import org.eclipse.smarthome.automation.type.ModuleType;
import org.eclipse.smarthome.automation.type.ModuleTypeProvider;
import org.eclipse.smarthome.automation.type.TriggerType;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Yordan Mihaylov - Initial Contribution
 * @author Kai Kreuzer - refactored (managed) provider and registry implementation
 */
@SuppressWarnings("rawtypes")
public class ModuleTypeManager implements ServiceTrackerCustomizer {

    @SuppressWarnings("rawtypes")
    private ServiceTracker moduleTypeTracker;
    private RuleEngine ruleEngine;
    private Set<ModuleTypeProvider> providers = new HashSet<>();
    private BundleContext bc;

    /**
     * @param bc
     * @param re
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ModuleTypeManager(BundleContext bc, RuleEngine re) {
        this.bc = bc;
        this.ruleEngine = re;
        moduleTypeTracker = new ServiceTracker(bc, ModuleTypeProvider.class.getName(), this);
        moduleTypeTracker.open();
    }

    public <T extends ModuleType> T getType(String typeUID) {
        return getType(typeUID, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends ModuleType> T getType(String typeUID, Locale locale) {
        ModuleType mType = null;
        for (ModuleTypeProvider provider : providers) {
            mType = provider.getModuleType(typeUID, locale);
            if (mType != null) {
                return (T) createCopy(mType);
            }
        }

        return null;
    }

    public <T extends ModuleType> Collection<T> getTypesByTag(String tag) {
        return getTypesByTag(tag, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends ModuleType> Collection<T> getTypesByTag(String tag, Locale locale) {
        Collection<T> result = new ArrayList<T>(20);
        Collection<ModuleType> moduleTypes = null;
        for (ModuleTypeProvider provider : providers) {
            moduleTypes = provider.getModuleTypes(locale);
            if (moduleTypes != null) {
                for (Iterator<ModuleType> it = moduleTypes.iterator(); it.hasNext();) {
                    ModuleType mt = it.next();
                    if (tag != null) {
                        Collection<String> tags = mt.getTags();
                        if (tags != null && tags.contains(tag)) {
                            result.add((T) createCopy(mt));
                        }
                    } else {
                        result.add((T) createCopy(mt));
                    }
                }
            }
        }
        return result;
    }

    public <T extends ModuleType> Collection<T> getTypesByTags(Set<String> tags) {
        return getTypesByTags(tags, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends ModuleType> Collection<T> getTypesByTags(Set<String> tags, Locale locale) {
        Collection<T> result = new ArrayList<T>(20);
        Collection<ModuleType> moduleTypes = null;
        for (ModuleTypeProvider provider : providers) {
            moduleTypes = provider.getModuleTypes(locale);
            if (moduleTypes != null) {
                for (Iterator<ModuleType> it = moduleTypes.iterator(); it.hasNext();) {
                    ModuleType mt = it.next();
                    if (tags != null) {
                        Collection<String> rTags = mt.getTags();
                        for (Iterator<String> itt = rTags.iterator(); itt.hasNext();) {
                            String tag = itt.next();
                            if (tags.contains(tag)) {
                                result.add((T) createCopy(mt));
                                break;
                            }
                        }
                    } else {
                        result.add((T) createCopy(mt));
                    }
                }
            }
        }
        return result;
    }

    public <T extends ModuleType> Collection<T> getTypes(Class<T> classModuleType) {
        return getTypes(classModuleType, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends ModuleType> Collection<T> getTypes(Class<T> moduleType, Locale locale) {
        Collection<T> result = new ArrayList<T>(20);
        Collection<ModuleType> moduleTypes = null;
        for (ModuleTypeProvider provider : providers) {
            moduleTypes = provider.getModuleTypes(locale);
            if (moduleTypes != null) {
                for (Iterator<ModuleType> it = moduleTypes.iterator(); it.hasNext();) {
                    ModuleType mt = it.next();
                    if (moduleType != null) {
                        if (moduleType.getName().equals(mt.getClass().getName())) {
                            result.add((T) createCopy(mt));
                        }
                    } else {
                        result.add((T) createCopy(mt));
                    }
                }
            }
        }
        return result;
    }

    public void dispose() {
        if (moduleTypeTracker != null) {
            moduleTypeTracker.close();
            moduleTypeTracker = null;
        }
        providers.clear();
    }

    /**
     * @param template
     * @return copy of template
     */
    private ModuleType createCopy(ModuleType mType) {
        if (mType == null) {
            return null;
        }

        ModuleType result;
        if (mType instanceof CompositeTriggerType) {
            CompositeTriggerType m = (CompositeTriggerType) mType;
            result = new CompositeTriggerType(mType.getUID(), mType.getConfigurationDescription(), mType.getLabel(),
                    mType.getDescription(), mType.getTags(), mType.getVisibility(), m.getOutputs(), m.getModules());

        } else if (mType instanceof TriggerType) {
            TriggerType m = (TriggerType) mType;
            result = new TriggerType(mType.getUID(), mType.getConfigurationDescription(), mType.getLabel(),
                    mType.getDescription(), mType.getTags(), mType.getVisibility(), m.getOutputs());

        } else if (mType instanceof CompositeConditionType) {
            CompositeConditionType m = (CompositeConditionType) mType;
            result = new CompositeConditionType(mType.getUID(), mType.getConfigurationDescription(), mType.getLabel(),
                    mType.getDescription(), mType.getTags(), mType.getVisibility(), m.getInputs(), m.getModules());

        } else if (mType instanceof ConditionType) {
            ConditionType m = (ConditionType) mType;
            result = new ConditionType(mType.getUID(), mType.getConfigurationDescription(), mType.getLabel(),
                    mType.getDescription(), mType.getTags(), mType.getVisibility(), m.getInputs());

        } else if (mType instanceof CompositeActionType) {
            CompositeActionType m = (CompositeActionType) mType;
            result = new CompositeActionType(mType.getUID(), mType.getConfigurationDescription(), mType.getLabel(),
                    mType.getDescription(), mType.getTags(), mType.getVisibility(), m.getInputs(), m.getOutputs(),
                    m.getModules());

        } else if (mType instanceof ActionType) {
            ActionType m = (ActionType) mType;
            result = new ActionType(mType.getUID(), mType.getConfigurationDescription(), mType.getLabel(),
                    mType.getDescription(), mType.getTags(), mType.getVisibility(), m.getInputs(), m.getOutputs());

        } else {
            throw new IllegalArgumentException("Invalid template type:" + mType);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object addingService(ServiceReference reference) {
        ModuleTypeProvider provider = (ModuleTypeProvider) bc.getService(reference);
        if (provider != null) {
            providers.add(provider);
            ruleEngine.moduleTypeUpdated(provider.getModuleTypes(null));
        }
        return provider;
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service) {
        @SuppressWarnings("unchecked")
        ModuleTypeProvider provider = (ModuleTypeProvider) bc.getService(reference);
        if (provider != null) {
            ruleEngine.moduleTypeUpdated(provider.getModuleTypes(null));
        }
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        providers.remove(service);
    }

}
