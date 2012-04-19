/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.billing.entitlement.api.user;

import com.ning.billing.catalog.api.Plan;
import com.ning.billing.catalog.api.PlanPhase;
import com.ning.billing.entitlement.api.SubscriptionTransitionType;
import com.ning.billing.entitlement.api.user.Subscription.SubscriptionState;
import com.ning.billing.entitlement.events.EntitlementEvent.EventType;
import com.ning.billing.entitlement.events.user.ApiEventType;
import com.ning.billing.entitlement.exceptions.EntitlementError;
import com.ning.billing.util.bus.BusEvent.BusEventType;

import org.joda.time.DateTime;

import java.util.UUID;

public class SubscriptionTransitionData implements SubscriptionEventTransition {


    private final long totalOrdering;
    private final UUID subscriptionId;
    private final UUID bundleId;
    private final UUID eventId;
    private final EventType eventType;
    private final ApiEventType apiEventType;
    private final DateTime requestedTransitionTime;
    private final DateTime effectiveTransitionTime;
    private final SubscriptionState previousState;
    private final String previousPriceList;
    private final Plan previousPlan;
    private final PlanPhase previousPhase;
    private final SubscriptionState nextState;
    private final String nextPriceList;
    private final Plan nextPlan;
    private final PlanPhase nextPhase;
    private final boolean isFromDisk;
    private final int remainingEventsForUserOperation;
    private final UUID userToken;

    public SubscriptionTransitionData(UUID eventId, UUID subscriptionId, UUID bundleId, EventType eventType,
            ApiEventType apiEventType, DateTime requestedTransitionTime, DateTime effectiveTransitionTime,
            SubscriptionState previousState, Plan previousPlan, PlanPhase previousPhase, String previousPriceList,
            SubscriptionState nextState, Plan nextPlan, PlanPhase nextPhase, String nextPriceList,
            long totalOrdering, UUID userToken, boolean isFromDisk) {
        super();
        this.eventId = eventId;
        this.subscriptionId = subscriptionId;
        this.bundleId = bundleId;
        this.eventType = eventType;
        this.apiEventType = apiEventType;
        this.requestedTransitionTime = requestedTransitionTime;
        this.effectiveTransitionTime = effectiveTransitionTime;
        this.previousState = previousState;
        this.previousPriceList = previousPriceList;
        this.previousPlan = previousPlan;
        this.previousPhase = previousPhase;
        this.nextState = nextState;
        this.nextPlan = nextPlan;
        this.nextPriceList = nextPriceList;
        this.nextPhase = nextPhase;
        this.totalOrdering = totalOrdering;
        this.isFromDisk = isFromDisk;
        this.userToken = userToken;
        this.remainingEventsForUserOperation = 0;
    }
    
    public SubscriptionTransitionData(final SubscriptionTransitionData input, final int remainingEventsForUserOperation) {
        super();
        this.eventId = input.getId();
        this.subscriptionId = input.getSubscriptionId();
        this.bundleId = input.getBundleId();
        this.eventType = input.getEventType();
        this.apiEventType = input.getApiEventType();
        this.requestedTransitionTime = input.getRequestedTransitionTime();
        this.effectiveTransitionTime = input.getEffectiveTransitionTime();
        this.previousState = input.getPreviousState();
        this.previousPriceList = input.getPreviousPriceList();
        this.previousPlan = input.getPreviousPlan();
        this.previousPhase = input.getPreviousPhase();
        this.nextState = input.getNextState();
        this.nextPlan = input.getNextPlan();
        this.nextPriceList = input.getNextPriceList();
        this.nextPhase = input.getNextPhase();
        this.totalOrdering = input.getTotalOrdering();
        this.isFromDisk = input.isFromDisk();
        this.userToken = input.getUserToken();
        this.remainingEventsForUserOperation = remainingEventsForUserOperation;
    }

	@Override
	public BusEventType getBusEventType() {
		return BusEventType.SUBSCRIPTION_TRANSITION;
	}


    @Override
    public UUID getId() {
        return eventId;
    }

    @Override
    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    @Override
    public UUID getBundleId() {
        return bundleId;
    }


    @Override
    public SubscriptionState getPreviousState() {
        return previousState;
    }

    @Override
    public Plan getPreviousPlan() {
        return previousPlan;
    }

    @Override
    public PlanPhase getPreviousPhase() {
        return previousPhase;
    }

    @Override
    public Plan getNextPlan() {
        return nextPlan;
    }

    @Override
    public PlanPhase getNextPhase() {
        return nextPhase;
    }

    @Override
    public SubscriptionState getNextState() {
        return nextState;
    }


    @Override
    public String getPreviousPriceList() {
        return previousPriceList;
    }

    @Override
    public String getNextPriceList() {
        return nextPriceList;
    }
    
	@Override
	public UUID getUserToken() {
		return userToken;
	}
	
	@Override
	public Integer getRemainingEventsForUserOperation() {
		return remainingEventsForUserOperation;
	}


    @Override
    public SubscriptionTransitionType getTransitionType() {
        return toSubscriptionTransitionType(eventType, apiEventType);
    }
    
    public static SubscriptionTransitionType toSubscriptionTransitionType(EventType eventType, ApiEventType apiEventType) {
        switch(eventType) {
        case API_USER:
            return apiEventType.getSubscriptionTransitionType();
        case PHASE:
            return SubscriptionTransitionType.PHASE;
        default:
            throw new EntitlementError("Unexpected event type " + eventType);
        }
    }

    @Override
    public DateTime getRequestedTransitionTime() {
        return requestedTransitionTime;
    }

    @Override
    public DateTime getEffectiveTransitionTime() {
        return effectiveTransitionTime;
    }

    public long getTotalOrdering() {
        return totalOrdering;
    }

    public boolean isFromDisk() {
        return isFromDisk;
    }

    public ApiEventType getApiEventType() {
        return apiEventType;
    }

    public EventType getEventType() {
        return eventType;
    }



    @Override
    public String toString() {
        return "SubscriptionTransition [eventId=" + eventId
            + ", subscriptionId=" + subscriptionId
            + ", eventType=" + eventType + ", apiEventType="
            + apiEventType + ", requestedTransitionTime=" + requestedTransitionTime
            + ", effectiveTransitionTime=" + effectiveTransitionTime
            + ", previousState=" + previousState + ", previousPlan="
            + ((previousPlan != null) ? previousPlan.getName()  : null)
            + ", previousPhase=" + ((previousPhase != null) ? previousPhase.getName() : null)
            + ", previousPriceList " + previousPriceList
            + ", nextState=" + nextState
            + ", nextPlan=" + ((nextPlan != null) ? nextPlan.getName() : null)
            + ", nextPriceList " + nextPriceList
            + ", nextPhase=" + ((nextPhase != null) ? nextPhase.getName() : null) + "]";
    }
}
