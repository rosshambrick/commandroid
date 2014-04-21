package com.rosshambrick.commandroid.tests.commands;

import com.rosshambrick.commandroid.Command;
import com.rosshambrick.commandroid.CommandListener;
import com.rosshambrick.commandroid.ThreadPoolCommandProcessor;
import com.rosshambrick.commandroid.tests.mocks.MockExecutor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class WhenCommandThrowsException implements CommandListener<Command> {
    private Command mFailedCommand;
    private Command mCompleteCommand;

    @Before
    public void setup() {
        ThreadPoolCommandProcessor commandProcessor = new ThreadPoolCommandProcessor(null, new MockExecutor());
        Command command = new Command() {
            @Override
            protected void execute() {
                throw new RuntimeException("Command error");
            }
        };
        commandProcessor.send(command, this);
    }

    @Override
    public void commandComplete(Command command) {
        mCompleteCommand = command;
    }

    @Override
    public void commandFailed(Command command) {
        mFailedCommand = command;
    }

    @Test
    public void shouldPublishErrorEvent() {
        assertNotNull(mFailedCommand);

        Exception error = mFailedCommand.getError();
        assertNotNull(error);

        assertTrue(error instanceof RuntimeException);

        String message = error.getMessage();
        assertEquals("Command error", message);
    }

    @Test
    public void shouldNotReturnSuccess() {
        assertNull(mCompleteCommand);
    }
}