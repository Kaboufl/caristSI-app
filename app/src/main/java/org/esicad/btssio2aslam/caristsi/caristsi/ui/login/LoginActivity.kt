package org.esicad.btssio2aslam.caristsi.caristsi.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.esicad.btssio2aslam.caristsi.caristsi.ui.home.HomeActivity
import org.esicad.btssio2aslam.caristsi.caristsi.ui.home.HomeActivityOLD
import org.esicad.btssio2aslam.caristsi.caristsi.ui.ui.theme.CaristSITheme

class LoginActivity : ComponentActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(org.esicad.btssio2aslam.caristsi.caristsi.R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(
            applicationContext,
            errorString,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = mutableStateOf("")
        val password = mutableStateOf("")
        val formValid = mutableStateOf(false)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            formValid.value = loginState.isDataValid
        })

        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }

            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        setContent {
            CaristSITheme {
                Surface {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 30.dp).padding(bottom = 220.dp)
                    ) {
                        LoginField(
                            value = username,
                            onChange = {
                                username.value = it
                                loginViewModel.loginDataChanged(username.value, password.value)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        PasswordField(
                            value = password.value,
                            onChange = {
                                password.value = it
                                loginViewModel.loginDataChanged(username.value, password.value)
                            },
                            submit = { loginViewModel.login(username.value, password.value) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = { loginViewModel.login(username.value, password.value) },
                            enabled = formValid.value,
                            modifier = Modifier
                        ) {
                            Text("Se connecter")
                        }
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginField(
    onChange: (String) -> Unit,
    value: MutableState<String>,
    modifier: Modifier = Modifier,
    label: String = "Adresse Email",
    placeholder: String = "Entrez votre adresse e-mail"
) {
    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Email,
            contentDescription = "Adresse e-mail",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    OutlinedTextField(
        value = value.value,
        onValueChange = onChange,
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down)}
        ),
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier.border(BorderStroke(0.dp, MaterialTheme.colorScheme.primary)),
    label: String = "Mot de passe",
    placeholder: String = "Entrez votre mot de passe"
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Lock,
            contentDescription = "Mot de passe",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Filled.FavoriteBorder else Icons.Default.Favorite,
                contentDescription = "Révéler le mot de passe",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

