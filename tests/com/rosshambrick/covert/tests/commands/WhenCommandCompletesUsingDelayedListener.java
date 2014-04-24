//package com.rosshambrick.covert.tests.commands;
//
//import com.rosshambrick.commandroid.tests.mocks.MockUiThread;
//import com.rosshambrick.covert.CommandListener;
//import com.rosshambrick.covert.CovertAgent;
//import com.rosshambrick.covert.tests.mocks.MockCommand;
//import com.rosshambrick.covert.tests.mocks.MockDependencyInjector;
//import com.rosshambrick.covert.tests.mocks.MockExecutor;
//import org.junit.Before;
//import org.junit.Test;
//
//import static junit.framework.Assert.*;
//
//public class WhenCommandCompletesUsingDelayedListener implements CommandListener<MockCommand> {
//    private MockDependencyInjector mDependencyInjector;
//    private MockExecutor mExecutor;
//    private MockCommand mFirstCommand;
//    private MockCommand mSecondCommand;
//    private MockCommand mSuccessCommand;
//    private MockCommand mFirstListenerCommand;
//
//    @Before
//    public void setup() {
//        mDependencyInjector = new MockDependencyInjector();
//
//        mExecutor = new MockExecutor();
//        CovertAgent covertAgent = new CovertAgent(
//                mDependencyInjector,
//                mExecutor,
//                new MockUiThread());
//
//        mSecondCommand = new MockCommand();
//        mFirstCommand = new MockCommand(mSecondCommand);
//        mFirstCommand.setId(1);
//        covertAgent.send(mFirstCommand, new FirstListener());
////        covertAgent.resetListener(1, this);
////        covertAgent.send(mFirstCommand);
//    }
//
//    private class FirstListener implements CommandListener<MockCommand> {
//        @Override
//        public void commandComplete(MockCommand command) {
//            mFirstListenerCommand = command;
//        }
//    }
//
//    @Override
//    public void commandComplete(MockCommand command) {
//        mSuccessCommand = command;
//    }
//
//    @Test
//    public void shouldSendTwoCommands() {
//        assertEquals(2, mExecutor.getMessagesSentCount());
//    }
//
//    @Test
//    public void shouldExecuteFirstCommand() {
//        assertTrue(mFirstCommand.isCommandExecuted());
//    }
//
//    @Test
//    public void shouldExecuteNextCommand() {
//        assertTrue(mSecondCommand.isCommandExecuted());
//    }
//
//    @Test
//    public void shouldRunCommand() {
//        assertTrue(mFirstCommand.isCommandExecuted());
//    }
//
//    @Test
//    public void shouldReturnSuccessResult() {
//        assertNotNull(mSuccessCommand);
//    }
//
//    @Test
//    public void shouldNotReturnToFirstListener() {
//        assertNull(mFirstListenerCommand);
//    }
//
//    @Test
//    public void shouldInjectDependencies() {
//        assertEquals(mFirstCommand, mDependencyInjector.get(0));
//        assertEquals(mSecondCommand, mDependencyInjector.get(1));
//    }
//}