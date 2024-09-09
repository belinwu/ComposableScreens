package ir.erfansn.composablescreens.food.feature.cart

import android.util.Log
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import ir.erfansn.composablescreens.food.ui.FoodTheme
import ir.erfansn.composablescreens.food.ui.component.FoodFloatingScaffold
import ir.erfansn.composablescreens.food.ui.component.FoodTopBar
import ir.erfansn.composablescreens.food.ui.component.VerticalHillButton
import ir.erfansn.composablescreens.food.ui.modifier.overlappedBackgroundColor
import ir.erfansn.composablescreens.food.ui.util.Cent
import ir.erfansn.composablescreens.food.ui.util.convertToDollars
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect

@Composable
fun CartRoute(
    viewModel: CartViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToProduct: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    CartScreen(
        onNavigateToHome = onNavigateToHome,
        onNavigateToProduct = onNavigateToProduct,
        modifier = modifier,
        cartProducts = viewModel.cartProducts,
        totalPrice = viewModel.productsTotalPrice
    )
}

@Composable
private fun CartScreen(
    cartProducts: List<CartProduct>,
    onNavigateToHome: () -> Unit,
    onNavigateToProduct: (id: Int) -> Unit,
    totalPrice: Int,
    modifier: Modifier = Modifier
) {
    // TODO: Sync predictive back gesture animation with custom pop up destination [https://issuetracker.google.com/issues/331809442]
    PredictiveBackHandler {
        try {
            it.collect()
            onNavigateToHome()
        } catch (e: CancellationException) {
            Log.i("CartScreen", "Back gesture cancelled")
        }
    }

    val lazyListState = rememberLazyListState()

    var paymentIsSuccessful by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center
    ) {
        FoodFloatingScaffold(
            topBar = {
                val isOverlapped by remember {
                    derivedStateOf {
                        lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 46
                    }
                }
                CartTopBar(
                    onNavigateToHome = onNavigateToHome,
                    modifier = Modifier.overlappedBackgroundColor(isOverlapped)
                )
            },
            floatingBottomBar = {
                if (cartProducts.isNotEmpty()) {
                    CartBottomBar(
                        totalPrice = totalPrice,
                        onPayClick = {
                            paymentIsSuccessful = true
                        },
                        contentPadding = it,
                        modifier = Modifier.consumeWindowInsets(it)
                    )
                }
            },
            modifier = modifier
                .fillMaxSize()
        ) {
            CartContent(
                cartProducts = cartProducts,
                contentPadding = it,
                state = lazyListState,
                onNavigateToProduct = onNavigateToProduct,
                modifier = Modifier.consumeWindowInsets(it)
            )
        }
        if (paymentIsSuccessful) {
            PaymentSuccessfulPane(
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun CartTopBar(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FoodTopBar(
        modifier = modifier,
        navigation = {
            VerticalHillButton(
                onClick = onNavigateToHome,
                title = "Catalog",
                modifier = Modifier
                    .rotate(180f)
            )
        },
        title = {
            Text(
                "Cart",
                style = FoodTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        action = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(FoodTheme.colors.primary)
            ) {
                Text(
                    "3",
                    style = FoodTheme.typography.titleLarge,
                    color = FoodTheme.colors.onPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    )
}

@Composable
private fun CartContent(
    cartProducts: List<CartProduct>,
    onNavigateToProduct: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = contentPadding,
        state = state
    ) {
        items(cartProducts) {
            CartProductItem(
                cartProduct = it,
                onClick = dropUnlessResumed { onNavigateToProduct(it.id) }
            )
        }
    }
}

@Composable
private fun CartBottomBar(
    totalPrice: Cent,
    onPayClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .pointerInput(Unit) {}
            .clip(CurvedShape)
            .background(FoodTheme.colors.primary)
            .padding(bottom = 8.dp, top = 24.dp)
            .padding(contentPadding)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(start = 48.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "Total amount",
                style = FoodTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                totalPrice.convertToDollars(),
                style = FoodTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(42.dp))
                .clickable { onPayClick() }
                .background(FoodTheme.colors.background)
                .padding(horizontal = 32.dp, vertical = 42.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides FoodTheme.colors.onBackground) {
                Text(
                    text = "Pay",
                    style = FoodTheme.typography.titleLarge,
                    modifier = Modifier
                        .offset(x = 6.dp)
                )
                repeat(3) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                        contentDescription = null,
                        tint = LocalContentColor.current.copy(alpha = 1f / (3 - it)),
                        modifier = Modifier
                            .size(24.dp)
                            .offset(x = 6.dp * (3 - 1 - it))
                    )
                }
            }
        }
    }
}

private val CurvedShape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val (width, height) = size
        return Outline.Generic(
            Path().apply {
                moveTo(
                    x = width * 0f,
                    y = height * 1f
                )
                lineTo(
                    x = width * 0f,
                    y = height * 0.35f
                )
                quadraticTo(
                    x1 = width * 0f,
                    y1 = height * 0.103f,
                    x2 = width * 0.08f,
                    y2 = height * 0.08f,
                )
                quadraticTo(
                    x1 = width * 0.5f,
                    y1 = height * -0.07f,
                    x2 = width - (width * 0.08f),
                    y2 = height * 0.08f,
                )
                quadraticTo(
                    x1 = width - (width * 0f),
                    y1 = height * 0.103f,
                    x2 = width - (width * 0f),
                    y2 = height * 0.35f,
                )
                lineTo(
                    x = width * 1f,
                    y = height * 1f
                )
            }
        )
    }
}

@Preview
@Composable
private fun CartScreenPreview() {
    FoodTheme {
        CartScreen(
            cartProducts = sampleCartProducts,
            onNavigateToHome = { },
            onNavigateToProduct = { },
            totalPrice = 8900
        )
    }
}
