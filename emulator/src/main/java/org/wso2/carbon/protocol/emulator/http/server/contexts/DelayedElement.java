/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.protocol.emulator.http.server.contexts;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Class for Delay element of the fast backend.
 */
public class DelayedElement implements Delayed {
    protected long timestamp;
    private long delay;
    private ChannelHandlerContext ctx;
    private HttpServerProcessorContext context;

    public DelayedElement(ChannelHandlerContext ctx, HttpServerProcessorContext context, long receivedTime, int delay) {
        this.delay = delay;
        this.ctx = ctx;
        this.context = context;
        this.timestamp = receivedTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return delay - (System.currentTimeMillis() - this.timestamp);
    }

    @Override
    public int compareTo(Delayed other) {
        long comparison = ((DelayedElement) other).timestamp - this.timestamp;
        if (comparison > 0) {
            return -1;
        } else if (comparison < 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DelayedElement that = (DelayedElement) o;
        if (delay != that.delay) {
            return false;
        }
        if (timestamp != that.timestamp) {
            return false;
        }
        if (ctx != null ? !ctx.equals(that.ctx) : that.ctx != null) {
            return false;
        }
        return !(context != null ? !context.equals(that.context) : that.context != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (delay ^ (delay >>> 32));
        result = 31 * result + (ctx != null ? ctx.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    public ChannelHandlerContext getContext() {
        return ctx;
    }

    public HttpServerProcessorContext getProcessorContext() {
        return this.context;
    }
}
