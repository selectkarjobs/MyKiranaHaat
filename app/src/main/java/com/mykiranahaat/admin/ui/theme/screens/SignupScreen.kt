package com.mykiranahaat.admin.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mykiranahaat.admin.viewmodel.AuthState
import com.mykiranahaat.admin.viewmodel.AuthViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    var showOtpField by remember { mutableStateOf(false) }
    val otpDigits = remember { List(6) { mutableStateOf("") } }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    var resendTimer by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Start timer when OTP field is shown
    LaunchedEffect(showOtpField) {
        if (showOtpField) {
            focusRequesters[0].requestFocus()
            resendTimer = 60
            canResend = false
            while (resendTimer > 0) {
                delay(1000)
                resendTimer--
            }
            canResend = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF6200EE), Color(0xFF3700B3))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(MaterialTheme.shapes.medium),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MyKiranaHaat Admin",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6200EE)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = viewModel.mobileNumber.value,
                    onValueChange = { viewModel.mobileNumber.value = it.take(10) },
                    label = { Text("Mobile Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF6200EE),
                        unfocusedIndicatorColor = Color.Gray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                AnimatedVisibility(
                    visible = showOtpField,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            otpDigits.forEachIndexed { index, digit ->
                                OutlinedTextField(
                                    value = digit.value,
                                    onValueChange = { newValue ->
                                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                            digit.value = newValue
                                            viewModel.otp.value = otpDigits.joinToString("") { it.value }
                                            if (newValue.isNotEmpty() && index < 5) {
                                                focusRequesters[index + 1].requestFocus()
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(width = 44.dp, height = 44.dp)
                                        .focusRequester(focusRequesters[index]),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color(0xFF6200EE),
                                        unfocusedIndicatorColor = Color.Gray,
                                        focusedContainerColor = Color(0xFFECEFF1),
                                        unfocusedContainerColor = Color(0xFFECEFF1)
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(
                                onClick = {
                                    if (canResend) {
                                        viewModel.sendOtp(viewModel.mobileNumber.value, activity, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                                viewModel.verifyOtp(credential)
                                            }

                                            override fun onVerificationFailed(exception: com.google.firebase.FirebaseException) {
                                                viewModel.authState.value = AuthState.Error(exception.message ?: "Verification failed")
                                            }

                                            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                                                viewModel.verificationId.value = verificationId
                                                showOtpField = true
                                                resendTimer = 60
                                                canResend = false
                                                coroutineScope.launch {
                                                    while (resendTimer > 0) {
                                                        delay(1000)
                                                        resendTimer--
                                                    }
                                                    canResend = true
                                                }
                                            }
                                        })
                                    }
                                },
                                enabled = canResend
                            ) {
                                Text(
                                    text = if (canResend) "Resend OTP" else "Resend OTP in $resendTimer s",
                                    color = if (canResend) Color(0xFF6200EE) else Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (!showOtpField) {
                            viewModel.sendOtp(viewModel.mobileNumber.value, activity, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                    viewModel.verifyOtp(credential)
                                }

                                override fun onVerificationFailed(exception: com.google.firebase.FirebaseException) {
                                    viewModel.authState.value = AuthState.Error(exception.message ?: "Verification failed")
                                }

                                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                                    viewModel.verificationId.value = verificationId
                                    showOtpField = true
                                }
                            })
                        } else {
                            if (viewModel.otp.value.length == 6) {
                                val credential = PhoneAuthProvider.getCredential(viewModel.verificationId.value, viewModel.otp.value)
                                viewModel.verifyOtp(credential)
                            } else {
                                Toast.makeText(context, "Please enter a 6-digit OTP", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE),
                        contentColor = Color.White
                    )
                ) {
                    Text(if (showOtpField) "Verify OTP" else "Send OTP")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = "Already have an account? Login",
                        color = Color(0xFF6200EE)
                    )
                }

                AnimatedVisibility(
                    visible = viewModel.authState.value is AuthState.Loading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .size(48.dp),
                        color = Color(0xFF6200EE),
                        strokeWidth = 4.dp
                    )
                }

                when (val state = viewModel.authState.value) {
                    is AuthState.Error -> {
                        LaunchedEffect(state) {
                            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    is AuthState.Success -> {
                        LaunchedEffect(state) {
                            onSignupSuccess()
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}