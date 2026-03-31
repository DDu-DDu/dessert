async function apiPost(url, body) {
    const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body || {})
    });
    return response.json();
}

function openProductModal() {
    const modal = document.getElementById("productModal");
    if (modal) {
        modal.classList.add("active");
        document.body.classList.add("modal-open");
    }
}

function closeProductModal() {
    const modal = document.getElementById("productModal");
    if (modal) {
        modal.classList.remove("active");
        document.body.classList.remove("modal-open");
    }
}

function openAgreementModal() {
    const modal = document.getElementById("agreementModal");
    const agreementCheck = document.getElementById("agreementCheck");
    const finalSubmitBtn = document.getElementById("finalSubmitBtn");

    if (modal) {
        modal.classList.add("active");
        document.body.classList.add("modal-open");
    }

    if (agreementCheck) {
        agreementCheck.checked = false;
    }

    if (finalSubmitBtn) {
        finalSubmitBtn.disabled = true;
    }
}

function closeAgreementModal() {
    const modal = document.getElementById("agreementModal");
    if (modal) {
        modal.classList.remove("active");
        document.body.classList.remove("modal-open");
    }
}

function selectProduct(element) {
    const name = element.dataset.name || "";
    const allCards = document.querySelectorAll(".catalog-card");
    allCards.forEach(card => card.classList.remove("selected"));
    element.classList.add("selected");

    if (name.includes("두바이 쫀득쿠키")) {
        openProductModal();
    } else {
        alert("현재 데모에서는 두바이 쫀득쿠키 상품만 카드형 상세 창이 연결되어 있습니다.");
    }
}

window.addEventListener("click", (event) => {
    const productModal = document.getElementById("productModal");
    const agreementModal = document.getElementById("agreementModal");

    if (productModal && event.target === productModal) {
        closeProductModal();
    }

    if (agreementModal && event.target === agreementModal) {
        closeAgreementModal();
    }
});

window.addEventListener("keydown", (event) => {
    if (event.key === "Escape") {
        closeProductModal();
        closeAgreementModal();
    }
});

const orderForm = document.getElementById("orderForm");
const agreementCheck = document.getElementById("agreementCheck");
const finalSubmitBtn = document.getElementById("finalSubmitBtn");
const preOrderBtn = document.getElementById("preOrderBtn");

if (preOrderBtn) {
    preOrderBtn.addEventListener("click", () => {
        const customerName = document.getElementById("customerName")?.value.trim();
        const quantity = Number(document.getElementById("quantity")?.value);

        if (!customerName) {
            alert("고객사명을 입력해주세요.");
            return;
        }

        if (!quantity || quantity < 1) {
            alert("수량을 1개 이상 입력해주세요.");
            return;
        }

        openAgreementModal();
    });
}

if (agreementCheck && finalSubmitBtn) {
    agreementCheck.addEventListener("change", () => {
        finalSubmitBtn.disabled = !agreementCheck.checked;
    });
}

if (finalSubmitBtn) {
    finalSubmitBtn.addEventListener("click", async () => {
        const customerName = document.getElementById("customerName")?.value.trim();
        const quantity = Number(document.getElementById("quantity")?.value);

        if (!agreementCheck?.checked) {
            alert("검역 통과시 주문확정 조건에 동의가 필요합니다.");
            return;
        }

        const result = await apiPost("/api/orders", { customerName, quantity });
        alert(result.message);

        if (result.success) {
            closeAgreementModal();
            location.reload();
        }
    });
}

// 기존 submit 방식은 막아둠
if (orderForm) {
    orderForm.addEventListener("submit", (e) => {
        e.preventDefault();
    });
}

async function updateCustomsStatus(customsStatus) {
    const result = await apiPost("/api/admin/customs-status", { customsStatus });
    alert(result.message);
    if (result.success) location.reload();
}

async function updateSecuredStock() {
    const securedStock = Number(document.getElementById("securedStock").value);
    const result = await apiPost("/api/admin/secured-stock", { securedStock });
    alert(result.message);
    if (result.success) location.reload();
}

async function confirmOrders() {
    const button = document.getElementById("confirmOrdersBtn");
    const currentStatus = button?.dataset.status;

    if (currentStatus !== "CLEARED") {
        alert("주문 확정은 통관완료 상태에서만 가능합니다.");
        return;
    }

    const result = await apiPost("/api/admin/confirm-orders", {
        customsStatus: currentStatus
    });

    alert(result.message);
    if (result.success) location.reload();
}

async function cancelOrder(orderId) {
    const result = await apiPost(`/api/orders/${orderId}/cancel`);
    alert(result.message);
    if (result.success) location.reload();
}

window.addEventListener("load", () => {
    const hideUntil = localStorage.getItem("hideNoticeUntil");
    const now = new Date().getTime();

    if (!hideUntil || now > Number(hideUntil)) {
        const modal = document.getElementById("noticeModal");
        if (modal) {
            modal.classList.add("show");
        }
    }
});

function closeNotice() {
    const modal = document.getElementById("noticeModal");
    if (modal) {
        modal.classList.remove("show");
    }
}

function hideNoticeToday() {
    const tomorrow = new Date();
    tomorrow.setHours(23, 59, 59, 999);
    localStorage.setItem("hideNoticeUntil", String(tomorrow.getTime()));
    closeNotice();
}

