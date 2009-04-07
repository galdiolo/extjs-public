/*
 * Ext Core Library 3.0 Beta
 * http://extjs.com/
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * 
 * The MIT License
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 */


Ext.util.TaskRunner=function(interval){interval=interval||10;var tasks=[],removeQueue=[],id=0,running=false,stopThread=function(){running=false;clearInterval(id);id=0;},startThread=function(){if(!running){running=true;id=setInterval(runTasks,interval);}},removeTask=function(t){removeQueue.push(t);if(t.onStop){t.onStop.apply(t.scope||t);}},runTasks=function(){var rqLen=removeQueue.length,now=new Date().getTime();if(rqLen>0){for(var i=0;i<rqLen;i++){tasks.remove(removeQueue[i]);}
removeQueue=[];if(tasks.length<1){stopThread();return;}}
for(var i=0,t,itime,rt,len=tasks.length;i<len;++i){t=tasks[i];itime=now-t.taskRunTime;if(t.interval<=itime){rt=t.run.apply(t.scope||t,t.args||[++t.taskRunCount]);t.taskRunTime=now;if(rt===false||t.taskRunCount===t.repeat){removeTask(t);return;}}
if(t.duration&&t.duration<=(now-t.taskStartTime)){removeTask(t);}}};this.start=function(task){tasks.push(task);task.taskStartTime=new Date().getTime();task.taskRunTime=0;task.taskRunCount=0;startThread();return task;};this.stop=function(task){removeTask(task);return task;};this.stopAll=function(){stopThread();for(var i=0,len=tasks.length;i<len;i++){if(tasks[i].onStop){tasks[i].onStop();}}
tasks=[];removeQueue=[];};};Ext.TaskMgr=new Ext.util.TaskRunner();