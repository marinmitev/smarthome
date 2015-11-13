/**
 * Copyright (c) 2010-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.module.core.handler;

import java.util.Map;

import org.eclipse.smarthome.automation.Trigger;
import org.eclipse.smarthome.automation.handler.RuleEngineCallback;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * This is an Job implementation which encapsulates a {@code RuleEngineCallback} in order to trigger a {@code Trigger}.
 * {@see TimerTriggerHandler}
 *
 * @author Christoph Knauf - Initial Contribution
 *
 */
public class CallbackJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(TimerTriggerHandler.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerContext schedulerContext = null;
        try {
            schedulerContext = context.getScheduler().getContext();
        } catch (SchedulerException e1) {
            logger.error("Can't get scheduler context");
        }
        RuleEngineCallback callback = (RuleEngineCallback) schedulerContext
                .get(TimerTriggerHandler.CALLBACK_CONTEXT_NAME);
        Trigger module = (Trigger) schedulerContext.get(TimerTriggerHandler.MODULE_CONTEXT_NAME);
        if (callback != null) {
            Map<String, Object> values = Maps.newHashMap();
            callback.triggered(module, values);
        }
    }

}
