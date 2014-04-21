package com.rosshambrick.commandroid;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("ALL")
public class ThreadPoolCommandProcessor implements CommandProcessor {
    public static final String TAG = "ThreadPoolCommandProcessor";

    private Executor mExecutor;
    private DependencyInjector mDependencyInjector;

    private Map<Class, Object> mLoadedMap = new HashMap<Class, Object>();
    private Map<Class<? extends Command>, List<CommandListener>> mSubscriberMap = new HashMap<Class<? extends Command>, List<CommandListener>>();
    private List<Command> mDelayedResultCommands = new ArrayList<Command>();

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, Executor executor) {
        mExecutor = executor;
        mDependencyInjector = dependencyInjector;
    }

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector, int threads) {
        this(dependencyInjector, Executors.newFixedThreadPool(threads));
    }

    public ThreadPoolCommandProcessor(Executor executor) {
        this(null, executor);
    }

    public ThreadPoolCommandProcessor(DependencyInjector dependencyInjector) {
        this(dependencyInjector, Executors.newSingleThreadExecutor());
    }

    public ThreadPoolCommandProcessor() {
        this(Executors.newSingleThreadExecutor());
    }

    @Override
    public UUID send(final Command command) {
        return send(command, null);
    }

    @Override
    public UUID send(final Command command, final CommandListener listener) {
        command.setCommandProcessor(this);
        command.setId(UUID.randomUUID());

        if (mDependencyInjector != null) {
            mDependencyInjector.inject(command);
        }

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.executeInternal();
                    if (listener != null) {
                        listener.commandComplete(command);
                    }
                } catch (Exception e) {
                    command.setError(e);
                    if (listener != null) {
                        listener.commandFailed(command);
                    }
                } finally {
                    if (listener == null) {
                        synchronized (mDelayedResultCommands) {
                            mDelayedResultCommands.add(command);
                            if (mDelayedResultCommands.size() > 10) {
                                mDelayedResultCommands.remove(0);
                            }
                        }
                    }
                }
            }
        });

        return command.getId();
    }

    @Override
    public void retryListener(UUID commandId, CommandListener listener) {
        synchronized (mDelayedResultCommands) {
            Command foundCommand = null;

            for (Command command : mDelayedResultCommands) {
                if (command.getId().equals(commandId)) {
                    foundCommand = command;
                    if (command.getError() == null) {
                        listener.commandComplete(command);
                    } else {
                        listener.commandFailed(command);
                    }
                }
            }
//            TODO: since we are limiting the delayed commands, do we need to clean up here?
//            if (foundCommand != null) {
//                mDelayedResultCommands.remove(foundCommand);
//            }
        }
    }

    @Override
    public <T> void load(final Query<T> query, final LoadListener<T> listener) {
        T cachedData = (T) mLoadedMap.get(query.getClass());
        if (cachedData != null) {
            if (listener != null) {
                listener.loadComplete(cachedData);
            }
        } else {
            doLoad(query, listener);
        }
    }

    @Override
    public <T> void load(Query<T> query) {
        load(query, null);
    }

    @Override
    public <T> void reload(Query<T> query, LoadListener<T> listener) {
        doLoad(query, listener);
    }

    private <T> void doLoad(final Query<T> query, final LoadListener<T> listener) {
        if (mDependencyInjector != null) {
            mDependencyInjector.inject(query);
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    T loadedData = query.loadInternal();
                    mLoadedMap.put(query.getClass(), loadedData);

                    //TODO:
                    // handle running this on the main thread
                    // so the Fragment doesn't have to?
                    if (listener != null) {
                        listener.loadComplete(loadedData);
                    }
                } catch (Exception e) {
                    query.setError(e);
                    if (listener != null) {
                        listener.loadFailed(query);
                    }
                }
            }
        });
    }

}
