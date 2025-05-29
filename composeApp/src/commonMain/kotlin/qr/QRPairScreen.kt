package qr

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import components.injectViewModel
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.QrShapes
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
internal fun QRPairScreen(
    viewModel: QRPairViewModel = injectViewModel(),
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pairingState by viewModel
        .viewState<QRPairViewState.Pairing>()
        .collectAsState(QRPairViewState.Pairing.initial)
    val connectingState by viewModel
        .viewState<QRPairViewState.Connecting>()
        .collectAsState(QRPairViewState.Connecting.initial)
    val closingState by viewModel
        .viewState<QRPairViewState.Closing>()
        .collectAsState(QRPairViewState.Closing.initial)

    val qrcodePainter = rememberQrCodePainter(
        data = pairingState.pairInfo,
        shapes = QrShapes(
            darkPixel = QrPixelShape.Default
        )
    )

    LaunchedEffect(key1 = closingState) {
        if (closingState.closing) {
            onDismissRequest()
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(
                    horizontal = 16.dp,
                    vertical = 32.dp,
                ),
        ) {
            Text(
                text = "Only for Android 11+ devices:\n" +
                        "1. Navigate to Developer options -> Wireless debugging -> Pair using QR code\n" +
                        "2. Scan the QR code below",
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = qrcodePainter,
                contentDescription = "Qr code pairing info.",
                modifier = Modifier.size(300.dp),
            )
        }

        AnimatedVisibility(
            visible = connectingState.visible,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}
