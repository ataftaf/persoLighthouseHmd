package org.tensorflow.lite.examples.transfer.databinding;
import org.tensorflow.lite.examples.transfer.R;
import org.tensorflow.lite.examples.transfer.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class CameraFragmentBindingImpl extends CameraFragmentBinding implements org.tensorflow.lite.examples.transfer.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.view_finder, 12);
        sViewsWithIds.put(R.id.classes_bar, 13);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @NonNull
    private final android.widget.TextView mboundView11;
    @NonNull
    private final android.widget.TextView mboundView2;
    @NonNull
    private final android.widget.TextView mboundView5;
    @NonNull
    private final android.widget.LinearLayout mboundView6;
    @NonNull
    private final android.widget.TextView mboundView7;
    @NonNull
    private final android.widget.TextView mboundView9;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback2;
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    @Nullable
    private final android.view.View.OnClickListener mCallback3;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public CameraFragmentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private CameraFragmentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 7
            , (android.widget.LinearLayout) bindings[8]
            , (android.widget.LinearLayout) bindings[10]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.LinearLayout) bindings[4]
            , (android.widget.LinearLayout) bindings[3]
            , (android.view.TextureView) bindings[12]
            );
        this.classBtn1.setTag(null);
        this.classBtn2.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView11 = (android.widget.TextView) bindings[11];
        this.mboundView11.setTag("subtitle");
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
        this.mboundView5 = (android.widget.TextView) bindings[5];
        this.mboundView5.setTag(null);
        this.mboundView6 = (android.widget.LinearLayout) bindings[6];
        this.mboundView6.setTag(null);
        this.mboundView7 = (android.widget.TextView) bindings[7];
        this.mboundView7.setTag(null);
        this.mboundView9 = (android.widget.TextView) bindings[9];
        this.mboundView9.setTag("subtitle");
        this.pauseButton.setTag(null);
        this.trainButton.setTag(null);
        setRootTag(root);
        // listeners
        mCallback2 = new org.tensorflow.lite.examples.transfer.generated.callback.OnClickListener(this, 2);
        mCallback1 = new org.tensorflow.lite.examples.transfer.generated.callback.OnClickListener(this, 1);
        mCallback3 = new org.tensorflow.lite.examples.transfer.generated.callback.OnClickListener(this, 3);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x100L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.vm == variableId) {
            setVm((org.tensorflow.lite.examples.transfer.CameraFragmentViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setVm(@Nullable org.tensorflow.lite.examples.transfer.CameraFragmentViewModel Vm) {
        this.mVm = Vm;
        synchronized(this) {
            mDirtyFlags |= 0x80L;
        }
        notifyPropertyChanged(BR.vm);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeVmNeededSamples((androidx.lifecycle.LiveData<java.lang.Integer>) object, fieldId);
            case 1 :
                return onChangeVmConfidence((androidx.lifecycle.LiveData<java.util.Map<java.lang.String,java.lang.Float>>) object, fieldId);
            case 2 :
                return onChangeVmFirstChoice((androidx.lifecycle.LiveData<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeVmCaptureMode((androidx.lifecycle.MutableLiveData<java.lang.Boolean>) object, fieldId);
            case 4 :
                return onChangeVmTrainingState((androidx.lifecycle.LiveData<org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState>) object, fieldId);
            case 5 :
                return onChangeVmNumSamples((androidx.lifecycle.LiveData<java.util.Map<java.lang.String,java.lang.Integer>>) object, fieldId);
            case 6 :
                return onChangeVmLastLoss((androidx.lifecycle.LiveData<java.lang.Float>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeVmNeededSamples(androidx.lifecycle.LiveData<java.lang.Integer> VmNeededSamples, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmConfidence(androidx.lifecycle.LiveData<java.util.Map<java.lang.String,java.lang.Float>> VmConfidence, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmFirstChoice(androidx.lifecycle.LiveData<java.lang.String> VmFirstChoice, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmCaptureMode(androidx.lifecycle.MutableLiveData<java.lang.Boolean> VmCaptureMode, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmTrainingState(androidx.lifecycle.LiveData<org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState> VmTrainingState, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmNumSamples(androidx.lifecycle.LiveData<java.util.Map<java.lang.String,java.lang.Integer>> VmNumSamples, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeVmLastLoss(androidx.lifecycle.LiveData<java.lang.Float> VmLastLoss, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        boolean vmCaptureModeVmFirstChoiceJavaLangString1BooleanFalse = false;
        int androidxDatabindingViewDataBindingSafeUnboxVmNeededSamplesGetValue = 0;
        boolean vmNeededSamplesInt0 = false;
        boolean vmTrainingStateTrainingStatePAUSED = false;
        org.tensorflow.lite.examples.transfer.CameraFragmentViewModel vm = mVm;
        boolean vmCaptureMode = false;
        java.lang.Boolean vmCaptureModeGetValue = null;
        java.util.Map<java.lang.String,java.lang.Integer> vmNumSamplesGetValue = null;
        java.lang.String stringFormatJavaLangStringNeedDSamplesVmNeededSamples = null;
        androidx.lifecycle.LiveData<java.lang.Integer> vmNeededSamples = null;
        boolean vmCaptureModeVmNeededSamplesInt0BooleanFalse = false;
        java.lang.Integer vmNeededSamplesGetValue = null;
        androidx.lifecycle.LiveData<java.util.Map<java.lang.String,java.lang.Float>> vmConfidence = null;
        androidx.lifecycle.LiveData<java.lang.String> vmFirstChoice = null;
        boolean VmNeededSamplesInt01 = false;
        java.lang.Float vmConfidence2 = null;
        boolean androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue = false;
        boolean vmLastLossJavaLangObjectNull = false;
        java.lang.String stringFormatJavaLangStringLoss3fVmLastLoss = null;
        boolean vmNeededSamplesInt0VmTrainingStateTrainingStateNOTSTARTEDBooleanFalse = false;
        java.lang.String vmFirstChoiceGetValue = null;
        boolean vmCaptureModeVmFirstChoiceJavaLangString2BooleanFalse = false;
        boolean vmTrainingStateTrainingStateNOTSTARTED = false;
        androidx.lifecycle.MutableLiveData<java.lang.Boolean> VmCaptureMode1 = null;
        java.lang.Integer vmNumSamples1 = null;
        boolean vmFirstChoiceJavaLangString1 = false;
        org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState vmTrainingStateGetValue = null;
        java.lang.Float vmLastLossGetValue = null;
        androidx.lifecycle.LiveData<org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState> vmTrainingState = null;
        androidx.lifecycle.LiveData<java.util.Map<java.lang.String,java.lang.Integer>> vmNumSamples = null;
        androidx.lifecycle.LiveData<java.lang.Float> vmLastLoss = null;
        java.lang.Float vmConfidence1 = null;
        java.util.Map<java.lang.String,java.lang.Float> vmConfidenceGetValue = null;
        java.lang.Integer vmNumSamples2 = null;
        boolean vmFirstChoiceJavaLangString2 = false;

        if ((dirtyFlags & 0x1ffL) != 0) {


            if ((dirtyFlags & 0x191L) != 0) {

                    if (vm != null) {
                        // read vm.neededSamples
                        vmNeededSamples = vm.getNeededSamples();
                    }
                    updateLiveDataRegistration(0, vmNeededSamples);


                    if (vmNeededSamples != null) {
                        // read vm.neededSamples.getValue()
                        vmNeededSamplesGetValue = vmNeededSamples.getValue();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue())
                    androidxDatabindingViewDataBindingSafeUnboxVmNeededSamplesGetValue = androidx.databinding.ViewDataBinding.safeUnbox(vmNeededSamplesGetValue);


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue()) == 0
                    vmNeededSamplesInt0 = (androidxDatabindingViewDataBindingSafeUnboxVmNeededSamplesGetValue) == (0);
                if((dirtyFlags & 0x191L) != 0) {
                    if(vmNeededSamplesInt0) {
                            dirtyFlags |= 0x4000L;
                    }
                    else {
                            dirtyFlags |= 0x2000L;
                    }
                }
                if ((dirtyFlags & 0x181L) != 0) {

                        // read String.format("Need %d samples", vm.neededSamples.getValue())
                        stringFormatJavaLangStringNeedDSamplesVmNeededSamples = java.lang.String.format("Need %d samples", vmNeededSamplesGetValue);
                }
            }
            if ((dirtyFlags & 0x1aaL) != 0) {

                    if (vm != null) {
                        // read vm.confidence
                        vmConfidence = vm.getConfidence();
                        // read vm.numSamples
                        vmNumSamples = vm.getNumSamples();
                    }
                    updateLiveDataRegistration(1, vmConfidence);
                    updateLiveDataRegistration(5, vmNumSamples);


                    if (vmConfidence != null) {
                        // read vm.confidence.getValue()
                        vmConfidenceGetValue = vmConfidence.getValue();
                    }
                    if (vmNumSamples != null) {
                        // read vm.numSamples.getValue()
                        vmNumSamplesGetValue = vmNumSamples.getValue();
                    }


                    if (vmConfidenceGetValue != null) {
                        // read vm.confidence.getValue()["2"]
                        vmConfidence2 = vmConfidenceGetValue.get("2");
                        // read vm.confidence.getValue()["1"]
                        vmConfidence1 = vmConfidenceGetValue.get("1");
                    }
                    if (vmNumSamplesGetValue != null) {
                        // read vm.numSamples.getValue()["1"]
                        vmNumSamples1 = vmNumSamplesGetValue.get("1");
                        // read vm.numSamples.getValue()["2"]
                        vmNumSamples2 = vmNumSamplesGetValue.get("2");
                    }
            }
            if ((dirtyFlags & 0x1afL) != 0) {

                    if (vm != null) {
                        // read vm.captureMode
                        VmCaptureMode1 = vm.getCaptureMode();
                    }
                    updateLiveDataRegistration(3, VmCaptureMode1);


                    if (VmCaptureMode1 != null) {
                        // read vm.captureMode.getValue()
                        vmCaptureModeGetValue = VmCaptureMode1.getValue();
                    }


                    // read androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue())
                    androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue = androidx.databinding.ViewDataBinding.safeUnbox(vmCaptureModeGetValue);
                if((dirtyFlags & 0x189L) != 0) {
                    if(androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue) {
                            dirtyFlags |= 0x1000L;
                    }
                    else {
                            dirtyFlags |= 0x800L;
                    }
                }

                if ((dirtyFlags & 0x18cL) != 0) {

                        // read !androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue())
                        vmCaptureMode = !androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue;
                    if((dirtyFlags & 0x18cL) != 0) {
                        if(vmCaptureMode) {
                                dirtyFlags |= 0x400L;
                                dirtyFlags |= 0x10000L;
                        }
                        else {
                                dirtyFlags |= 0x200L;
                                dirtyFlags |= 0x8000L;
                        }
                    }
                }
                if ((dirtyFlags & 0x1aaL) != 0) {
                }
            }
            if ((dirtyFlags & 0x190L) != 0) {

                    if (vm != null) {
                        // read vm.trainingState
                        vmTrainingState = vm.getTrainingState();
                    }
                    updateLiveDataRegistration(4, vmTrainingState);


                    if (vmTrainingState != null) {
                        // read vm.trainingState.getValue()
                        vmTrainingStateGetValue = vmTrainingState.getValue();
                    }


                    // read vm.trainingState.getValue() == TrainingState.PAUSED
                    vmTrainingStateTrainingStatePAUSED = (vmTrainingStateGetValue) == (org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState.PAUSED);
            }
            if ((dirtyFlags & 0x1c0L) != 0) {

                    if (vm != null) {
                        // read vm.lastLoss
                        vmLastLoss = vm.getLastLoss();
                    }
                    updateLiveDataRegistration(6, vmLastLoss);


                    if (vmLastLoss != null) {
                        // read vm.lastLoss.getValue()
                        vmLastLossGetValue = vmLastLoss.getValue();
                    }


                    // read vm.lastLoss.getValue() != null
                    vmLastLossJavaLangObjectNull = (vmLastLossGetValue) != (null);
                    // read String.format("Loss: %.3f", vm.lastLoss.getValue())
                    stringFormatJavaLangStringLoss3fVmLastLoss = java.lang.String.format("Loss: %.3f", vmLastLossGetValue);
            }
        }
        // batch finished

        if ((dirtyFlags & 0x1000L) != 0) {

                if (vm != null) {
                    // read vm.neededSamples
                    vmNeededSamples = vm.getNeededSamples();
                }
                updateLiveDataRegistration(0, vmNeededSamples);


                if (vmNeededSamples != null) {
                    // read vm.neededSamples.getValue()
                    vmNeededSamplesGetValue = vmNeededSamples.getValue();
                }


                // read androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue())
                androidxDatabindingViewDataBindingSafeUnboxVmNeededSamplesGetValue = androidx.databinding.ViewDataBinding.safeUnbox(vmNeededSamplesGetValue);


                // read androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue()) > 0
                VmNeededSamplesInt01 = (androidxDatabindingViewDataBindingSafeUnboxVmNeededSamplesGetValue) > (0);
        }
        if ((dirtyFlags & 0x10400L) != 0) {

                if (vm != null) {
                    // read vm.firstChoice
                    vmFirstChoice = vm.getFirstChoice();
                }
                updateLiveDataRegistration(2, vmFirstChoice);


                if (vmFirstChoice != null) {
                    // read vm.firstChoice.getValue()
                    vmFirstChoiceGetValue = vmFirstChoice.getValue();
                }

            if ((dirtyFlags & 0x400L) != 0) {

                    // read vm.firstChoice.getValue() == "1"
                    vmFirstChoiceJavaLangString1 = (vmFirstChoiceGetValue) == ("1");
            }
            if ((dirtyFlags & 0x10000L) != 0) {

                    // read vm.firstChoice.getValue() == "2"
                    vmFirstChoiceJavaLangString2 = (vmFirstChoiceGetValue) == ("2");
            }
        }
        if ((dirtyFlags & 0x4000L) != 0) {

                if (vm != null) {
                    // read vm.trainingState
                    vmTrainingState = vm.getTrainingState();
                }
                updateLiveDataRegistration(4, vmTrainingState);


                if (vmTrainingState != null) {
                    // read vm.trainingState.getValue()
                    vmTrainingStateGetValue = vmTrainingState.getValue();
                }


                // read vm.trainingState.getValue() == TrainingState.NOT_STARTED
                vmTrainingStateTrainingStateNOTSTARTED = (vmTrainingStateGetValue) == (org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState.NOT_STARTED);
        }

        if ((dirtyFlags & 0x18cL) != 0) {

                // read !androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? vm.firstChoice.getValue() == "1" : false
                vmCaptureModeVmFirstChoiceJavaLangString1BooleanFalse = ((vmCaptureMode) ? (vmFirstChoiceJavaLangString1) : (false));
                // read !androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? vm.firstChoice.getValue() == "2" : false
                vmCaptureModeVmFirstChoiceJavaLangString2BooleanFalse = ((vmCaptureMode) ? (vmFirstChoiceJavaLangString2) : (false));
        }
        if ((dirtyFlags & 0x189L) != 0) {

                // read androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue()) > 0 : false
                vmCaptureModeVmNeededSamplesInt0BooleanFalse = ((androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue) ? (VmNeededSamplesInt01) : (false));
        }
        if ((dirtyFlags & 0x191L) != 0) {

                // read androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue()) == 0 ? vm.trainingState.getValue() == TrainingState.NOT_STARTED : false
                vmNeededSamplesInt0VmTrainingStateTrainingStateNOTSTARTEDBooleanFalse = ((vmNeededSamplesInt0) ? (vmTrainingStateTrainingStateNOTSTARTED) : (false));
        }
        // batch finished
        if ((dirtyFlags & 0x18cL) != 0) {
            // api target 1

            org.tensorflow.lite.examples.transfer.CameraFragment.setClassButtonHighlight(this.classBtn1, vmCaptureModeVmFirstChoiceJavaLangString1BooleanFalse);
            org.tensorflow.lite.examples.transfer.CameraFragment.setClassButtonHighlight(this.classBtn2, vmCaptureModeVmFirstChoiceJavaLangString2BooleanFalse);
        }
        if ((dirtyFlags & 0x188L) != 0) {
            // api target 1

            this.classBtn1.setClickable(androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue);
            this.classBtn1.setEnabled(androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue);
            this.classBtn2.setClickable(androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue);
            this.classBtn2.setEnabled(androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue);
        }
        if ((dirtyFlags & 0x189L) != 0) {
            // api target 1

            org.tensorflow.lite.examples.transfer.CameraFragment.setViewVisibility(this.mboundView1, vmCaptureModeVmNeededSamplesInt0BooleanFalse);
        }
        if ((dirtyFlags & 0x1aaL) != 0) {
            // api target 1

            org.tensorflow.lite.examples.transfer.CameraFragment.setClassSubtitleText(this.mboundView11, androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue, vmConfidence2, vmNumSamples2);
            org.tensorflow.lite.examples.transfer.CameraFragment.setClassSubtitleText(this.mboundView9, androidxDatabindingViewDataBindingSafeUnboxVmCaptureModeGetValue, vmConfidence1, vmNumSamples1);
        }
        if ((dirtyFlags & 0x181L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView2, stringFormatJavaLangStringNeedDSamplesVmNeededSamples);
        }
        if ((dirtyFlags & 0x1c0L) != 0) {
            // api target 1

            org.tensorflow.lite.examples.transfer.CameraFragment.setViewVisibility(this.mboundView5, vmLastLossJavaLangObjectNull);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView5, stringFormatJavaLangStringLoss3fVmLastLoss);
            org.tensorflow.lite.examples.transfer.CameraFragment.setViewVisibility(this.mboundView7, vmLastLossJavaLangObjectNull);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView7, stringFormatJavaLangStringLoss3fVmLastLoss);
        }
        if ((dirtyFlags & 0x190L) != 0) {
            // api target 1

            org.tensorflow.lite.examples.transfer.CameraFragment.setViewVisibility(this.mboundView6, vmTrainingStateTrainingStatePAUSED);
        }
        if ((dirtyFlags & 0x100L) != 0) {
            // api target 1

            this.mboundView6.setOnClickListener(mCallback3);
            this.pauseButton.setOnClickListener(mCallback2);
            this.trainButton.setOnClickListener(mCallback1);
        }
        if ((dirtyFlags & 0x191L) != 0) {
            // api target 1

            org.tensorflow.lite.examples.transfer.CameraFragment.setViewVisibility(this.trainButton, vmNeededSamplesInt0VmTrainingStateTrainingStateNOTSTARTEDBooleanFalse);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // vm != null
                boolean vmJavaLangObjectNull = false;
                // vm
                org.tensorflow.lite.examples.transfer.CameraFragmentViewModel vm = mVm;



                vmJavaLangObjectNull = (vm) != (null);
                if (vmJavaLangObjectNull) {




                    vm.setTrainingState(org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState.PAUSED);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // vm != null
                boolean vmJavaLangObjectNull = false;
                // vm
                org.tensorflow.lite.examples.transfer.CameraFragmentViewModel vm = mVm;



                vmJavaLangObjectNull = (vm) != (null);
                if (vmJavaLangObjectNull) {




                    vm.setTrainingState(org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState.STARTED);
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // vm != null
                boolean vmJavaLangObjectNull = false;
                // vm
                org.tensorflow.lite.examples.transfer.CameraFragmentViewModel vm = mVm;



                vmJavaLangObjectNull = (vm) != (null);
                if (vmJavaLangObjectNull) {




                    vm.setTrainingState(org.tensorflow.lite.examples.transfer.CameraFragmentViewModel.TrainingState.STARTED);
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): vm.neededSamples
        flag 1 (0x2L): vm.confidence
        flag 2 (0x3L): vm.firstChoice
        flag 3 (0x4L): vm.captureMode
        flag 4 (0x5L): vm.trainingState
        flag 5 (0x6L): vm.numSamples
        flag 6 (0x7L): vm.lastLoss
        flag 7 (0x8L): vm
        flag 8 (0x9L): null
        flag 9 (0xaL): !androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? vm.firstChoice.getValue() == "1" : false
        flag 10 (0xbL): !androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? vm.firstChoice.getValue() == "1" : false
        flag 11 (0xcL): androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue()) > 0 : false
        flag 12 (0xdL): androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue()) > 0 : false
        flag 13 (0xeL): androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue()) == 0 ? vm.trainingState.getValue() == TrainingState.NOT_STARTED : false
        flag 14 (0xfL): androidx.databinding.ViewDataBinding.safeUnbox(vm.neededSamples.getValue()) == 0 ? vm.trainingState.getValue() == TrainingState.NOT_STARTED : false
        flag 15 (0x10L): !androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? vm.firstChoice.getValue() == "2" : false
        flag 16 (0x11L): !androidx.databinding.ViewDataBinding.safeUnbox(vm.captureMode.getValue()) ? vm.firstChoice.getValue() == "2" : false
    flag mapping end*/
    //end
}