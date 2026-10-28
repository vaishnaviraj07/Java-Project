
const $ = (id) => document.getElementById(id);

let liquids = [];
let usageRecords = [];

const menuToggle = $("menuToggle");
const sidebar = $("sidebar");

function closeMenu() {
    document.body.classList.remove("menu-open");
    document.body.classList.remove("sidebar-collapsed");
    menuToggle.setAttribute("aria-expanded", "false");
}

menuToggle.addEventListener("click", () => {
    if (window.innerWidth <= 768) {
        document.body.classList.toggle("menu-open");
        menuToggle.setAttribute(
            "aria-expanded",
            document.body.classList.contains("menu-open") ? "true" : "false"
        );
    } else {
        document.body.classList.toggle("sidebar-collapsed");
        menuToggle.setAttribute(
            "aria-expanded",
            document.body.classList.contains("sidebar-collapsed") ? "false" : "true"
        );
    }
});

// Navigate between pages
function showPage(pageId) {
    document.querySelectorAll(".page").forEach((page) => {
        page.classList.toggle("active", page.id === pageId);
    });

    document.querySelectorAll(".nav-link").forEach((button) => {
        button.classList.toggle("active", button.dataset.page === pageId);
    });

    closeMenu();
    window.scrollTo({ top: 0, behavior: "smooth" });
}

document.querySelectorAll(".nav-link").forEach((button) => {
    button.addEventListener("click", () => showPage(button.dataset.page));
});

document.querySelectorAll("[data-go]").forEach((button) => {
    button.addEventListener("click", () => showPage(button.dataset.go));
});

// Show messages below forms
function showMessage(elementId, message, isError = false) {
    const element = $(elementId);
    if (!element) return;
    element.textContent = message;
    element.classList.toggle("error", isError);
}

function showLeakMessage(message, isError = false) {
    let element = $("leakMessage");
    if (!element) {
        const tableBody = $("anomalyTableBody");
        const card = tableBody ? tableBody.closest(".content-card") : null;
        if (card) {
            element = document.createElement("p");
            element.id = "leakMessage";
            element.className = "message";
            element.setAttribute("aria-live", "polite");
            card.appendChild(element);
        }
    }

    if (element) {
        element.textContent = message;
        element.classList.toggle("error", isError);
    }
}


function formatNumber(number) {
    return Number(number).toLocaleString("en-IN", {
        maximumFractionDigits: 2
    });
}

// Register a liquid/container
$("registrationForm").addEventListener("submit", (event) => {
    event.preventDefault();

    const name = $("liquidName").value.trim();
    const id = $("containerId").value.trim();
    const capacity = Number($("capacity").value);

    if (!name || !id || !Number.isFinite(capacity) || capacity <= 0) {
        showMessage("registrationMessage", "Please enter valid details.", true);
        return;
    }

    const duplicate = liquids.some(
        (liquid) => liquid.id.toLowerCase() === id.toLowerCase()
    );

    if (duplicate) {
        showMessage("registrationMessage", "This Container ID is already registered.", true);
        return;
    }

    liquids.push({ name, id, capacity });

    $("registrationForm").reset();
    showMessage("registrationMessage", "Liquid registered successfully!");
    renderAll();
});

// Update the container dropdown
function renderContainerOptions() {
    const select = $("usageContainer");
    const previousValue = select.value;

    select.replaceChildren();

    if (liquids.length === 0) {
        select.add(new Option("Register a container first", ""));
        select.disabled = true;
        return;
    }

    select.disabled = false;
    select.add(new Option("Select a container", ""));

    liquids.forEach((liquid) => {
        select.add(new Option(
            `${liquid.name} — ${liquid.id}`,
            liquid.id
        ));
    });

    if (liquids.some((liquid) => liquid.id === previousValue)) {
        select.value = previousValue;
    }
}

// Save usage entry
$("usageForm").addEventListener("submit", (event) => {
    event.preventDefault();

    const containerId = $("usageContainer").value;
    const previous = Number($("previousReading").value);
    const current = Number($("currentReading").value);

    const liquid = liquids.find((item) => item.id === containerId);

    if (!liquid) {
        showMessage("usageMessage", "Please register and select a container first.", true);
        return;
    }

    if (
        !Number.isFinite(previous) ||
        !Number.isFinite(current) ||
        previous < 0 ||
        current < 0
    ) {
        showMessage("usageMessage", "Please enter valid non-negative readings.", true);
        return;
    }

    if (current > previous) {
        showMessage(
            "usageMessage",
            "Current reading cannot exceed previous reading in this demo. Check your readings.",
            true
        );
        return;
    }

    if (previous > liquid.capacity || current > liquid.capacity) {
        showMessage(
            "usageMessage",
            `Readings cannot exceed the container capacity of ${formatNumber(liquid.capacity)} L.`,
            true
        );
        return;
    }

    const consumed = previous - current;

    usageRecords.unshift({
        id: `rec_${Date.now()}_${Math.random().toString(36).slice(2, 7)}`,
        name: liquid.name,
        containerId: liquid.id,
        previous,
        current,
        consumed,
        time: new Date().toLocaleString(),
        reviewed: false
    });

    $("usageForm").reset();
    showMessage(
        "usageMessage",
        `Usage saved successfully. Consumption: ${formatNumber(consumed)} L`
    );

    renderAll();
});

// Standalone consumption calculator
$("calculatorForm").addEventListener("submit", (event) => {
    event.preventDefault();

    const previous = Number($("calcPrevious").value);
    const current = Number($("calcCurrent").value);
    const result = $("calculatorResult");

    if (
        !Number.isFinite(previous) ||
        !Number.isFinite(current) ||
        previous < 0 ||
        current < 0
    ) {
        result.querySelector("strong").textContent = "Invalid readings";
        return;
    }

    if (current > previous) {
        result.querySelector("strong").textContent =
            "Check readings: current is higher";
        return;
    }

    result.querySelector("strong").textContent =
        `${formatNumber(previous - current)} L`;
});

// Safe table cell creation
function addCell(row, value) {
    const cell = document.createElement("td");
    cell.textContent = String(value);
    row.appendChild(cell);
    return cell;
}

function showEmptyRow(bodyId, columnCount, message) {
    const body = $(bodyId);
    body.replaceChildren();

    const row = document.createElement("tr");
    const cell = document.createElement("td");

    cell.colSpan = columnCount;
    cell.className = "empty-state";
    cell.textContent = message;

    row.appendChild(cell);
    body.appendChild(row);
}

function renderLiquids() {
    const body = $("liquidTableBody");

    if (liquids.length === 0) {
        showEmptyRow("liquidTableBody", 3, "No containers registered.");
        return;
    }

    body.replaceChildren();

    liquids.forEach((liquid) => {
        const row = document.createElement("tr");
        addCell(row, liquid.name);
        addCell(row, liquid.id);
        addCell(row, `${formatNumber(liquid.capacity)} L`);
        body.appendChild(row);
    });
}

function renderUsageTable(bodyId, records, includeTime = false) {
    const body = $(bodyId);
    const columns = includeTime ? 6 : 5;

    if (records.length === 0) {
        showEmptyRow(
            bodyId,
            columns,
            "No usage entries yet."
        );
        return;
    }

    body.replaceChildren();

    records.forEach((record) => {
        const row = document.createElement("tr");

        addCell(row, record.name);
        addCell(row, record.containerId);
        addCell(row, formatNumber(record.previous));
        addCell(row, formatNumber(record.current));
        addCell(row, `${formatNumber(record.consumed)} L`);

        if (includeTime) {
            addCell(row, record.time);
        }

        body.appendChild(row);
    });
}

function renderAnomalies() {
    const body = $("anomalyTableBody");
    const flagged = usageRecords.filter((record) => record.consumed >= 50);

    if (flagged.length === 0) {
        showEmptyRow("anomalyTableBody", 4, "No high-usage entries to review.");
        return;
    }

    body.replaceChildren();

    flagged.forEach((record) => {
        if (!record.id) {
            record.id = `rec_${Date.now()}_${Math.random().toString(36).slice(2, 7)}`;
        }

        const row = document.createElement("tr");
        addCell(row, record.containerId);
        addCell(row, record.name);
        addCell(row, `${formatNumber(record.consumed)} L`);

        const cell = document.createElement("td");
        if (record.reviewed) {
            const badge = document.createElement("span");
            badge.className = "badge";
            badge.textContent = "Marked for review";
            cell.appendChild(badge);
        } else {
            const btn = document.createElement("button");
            btn.type = "button";
            btn.className = "badge warning";
            btn.style.border = "none";
            btn.style.cursor = "pointer";
            btn.textContent = "Review usage";
            btn.dataset.action = "review";
            btn.dataset.recordId = record.id;
            btn.setAttribute(
                "aria-label",
                `Mark high consumption for ${record.name} (${record.containerId}) for review`
            );
            cell.appendChild(btn);
        }
        row.appendChild(cell);

        body.appendChild(row);
    });
}

function renderDashboard() {
    $("registeredCount").textContent = liquids.length;

    const total = usageRecords.reduce(
        (sum, record) => sum + record.consumed,
        0
    );

    $("totalConsumption").textContent = `${formatNumber(total)} L`;

    const hasHighUsage = usageRecords.some(
        (record) => record.consumed >= 50
    );

    $("systemStatus").textContent = hasHighUsage ? "Review" : "Normal";
    $("systemStatus").classList.toggle("status-warning", hasHighUsage);
    $("systemStatus").classList.toggle("status-normal", !hasHighUsage);

    $("statusDescription").textContent = hasHighUsage
        ? "High usage entry needs review"
        : "No high usage recorded";

    renderUsageTable("recentUsageBody", usageRecords.slice(0, 5));
}

function renderAll() {
    renderContainerOptions();
    renderLiquids();
    renderDashboard();
    renderUsageTable("reportTableBody", usageRecords, true);
    renderAnomalies();
}

// Export report to CSV
$("exportBtn").addEventListener("click", () => {
    if (usageRecords.length === 0) {
        alert("There are no usage records to export yet.");
        return;
    }

    const rows = [
        ["Liquid", "Container ID", "Previous Reading (L)",
            "Current Reading (L)", "Consumption (L)", "Time"],
        ...usageRecords.map((record) => [
            record.name,
            record.containerId,
            record.previous,
            record.current,
            record.consumed,
            record.time
        ])
    ];

    const csv = rows.map((row) =>
        row.map((value) =>
            `"${String(value).replace(/"/g, '""')}"`
        ).join(",")
    ).join("\r\n");

    const blob = new Blob(["\uFEFF" + csv], {
        type: "text/csv;charset=utf-8;"
    });

    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");

    link.href = url;
    link.download = "liquid-usage-report.csv";
    document.body.appendChild(link);
    link.click();
    link.remove();
    URL.revokeObjectURL(url);
});

// Delegate review click event on anomaly table body (attached once to prevent duplicate listeners)
$("anomalyTableBody").addEventListener("click", (event) => {
    const btn = event.target.closest("button[data-action='review']");
    if (!btn) return;

    const recordId = btn.dataset.recordId;
    const record = usageRecords.find((r) => r.id === recordId);

    if (!record || record.reviewed) return;

    record.reviewed = true;

    showLeakMessage(
        `High-consumption entry for container ${record.containerId} (${record.name} — ${formatNumber(record.consumed)} L) has been marked for review.`
    );

    renderAnomalies();
});

// Reset temporary demo data (preserves session reset feature)
function resetSessionData() {
    const confirmed = confirm(
        "Reset this demo session? All registered liquids and usage entries will be cleared."
    );

    if (!confirmed) return;

    liquids = [];
    usageRecords = [];

    $("registrationForm").reset();
    $("usageForm").reset();
    $("calculatorForm").reset();

    showMessage("registrationMessage", "");
    showMessage("usageMessage", "");
    showLeakMessage("");
    $("calculatorResult").querySelector("strong").textContent = "—";

    renderAll();
    showPage("dashboard");
}

const resetSessionBtn = $("resetSessionBtn");
if (resetSessionBtn) {
    resetSessionBtn.addEventListener("click", resetSessionData);
}

// ==========================================================================
// Authentication & Login Page Logic (Demo Simulation)
// ==========================================================================

let isAuthenticated = false;

// Display validation or status messages in the login card
function showLoginMessage(message, isError = false, isSuccess = false) {
    const el = $("loginMessage");
    if (!el) return;

    if (!message) {
        el.textContent = "";
        el.className = "login-message";
        return;
    }

    el.textContent = message;
    el.className = "login-message show";
    if (isError) el.classList.add("error");
    if (isSuccess) el.classList.add("success");
}

// Switch between Login View and Dashboard View
function setLoginState(isLoggedIn, username = "admin") {
    isAuthenticated = isLoggedIn;

    if (isLoggedIn) {
        document.body.classList.remove("is-logged-out");
        document.body.classList.add("is-logged-in");

        const badgeName = $("loggedInUserName");
        if (badgeName) {
            badgeName.textContent = username;
        }

        renderAll();
        showPage("dashboard");
    } else {
        document.body.classList.remove("is-logged-in");
        document.body.classList.add("is-logged-out");

        closeMenu();

        const loginForm = $("loginForm");
        if (loginForm) loginForm.reset();

        showLoginMessage("");

        const usernameInput = $("loginUsername");
        const passwordInput = $("loginPassword");
        if (usernameInput) usernameInput.classList.remove("input-error");
        if (passwordInput) {
            passwordInput.classList.remove("input-error");
            passwordInput.type = "password";
        }

        const toggleBtn = $("togglePasswordBtn");
        if (toggleBtn) {
            toggleBtn.textContent = "👁";
            toggleBtn.setAttribute("aria-label", "Show password");
            toggleBtn.setAttribute("title", "Show password");
        }

        window.scrollTo({ top: 0, behavior: "smooth" });
    }
}

// Handle Login form submission with demo validation
const loginForm = $("loginForm");
if (loginForm) {
    loginForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const usernameInput = $("loginUsername");
        const passwordInput = $("loginPassword");
        const username = usernameInput ? usernameInput.value.trim() : "";
        const password = passwordInput ? passwordInput.value : "";

        if (usernameInput) usernameInput.classList.remove("input-error");
        if (passwordInput) passwordInput.classList.remove("input-error");

        // Validate empty fields
        if (!username && !password) {
            showLoginMessage("Please enter your username and password.", true);
            if (usernameInput) {
                usernameInput.classList.add("input-error");
                usernameInput.focus();
            }
            if (passwordInput) passwordInput.classList.add("input-error");
            return;
        }

        if (!username) {
            showLoginMessage("Please enter your username.", true);
            if (usernameInput) {
                usernameInput.classList.add("input-error");
                usernameInput.focus();
            }
            return;
        }

        if (!password) {
            showLoginMessage("Please enter your password.", true);
            if (passwordInput) {
                passwordInput.classList.add("input-error");
                passwordInput.focus();
            }
            return;
        }

        // Demo credentials check (admin / 1234, matching LoginFrame.java defaults)
        if (username.toLowerCase() === "admin" && password === "1234") {
            showLoginMessage("Login successful! Loading dashboard...", false, true);
            const submitBtn = $("loginSubmitBtn");
            if (submitBtn) submitBtn.disabled = true;

            setTimeout(() => {
                if (submitBtn) submitBtn.disabled = false;
                setLoginState(true, username);
            }, 350);
        } else {
            showLoginMessage("Invalid username or password. For demo, use: admin / 1234", true);
            if (passwordInput) {
                passwordInput.classList.add("input-error");
                passwordInput.focus();
            }
        }
    });
}

// Clear error messages when user modifies inputs
const usernameField = $("loginUsername");
if (usernameField) {
    usernameField.addEventListener("input", () => {
        usernameField.classList.remove("input-error");
        showLoginMessage("");
    });
}

const passwordField = $("loginPassword");
if (passwordField) {
    passwordField.addEventListener("input", () => {
        passwordField.classList.remove("input-error");
        showLoginMessage("");
    });
}

// Password visibility toggle
const togglePasswordBtn = $("togglePasswordBtn");
if (togglePasswordBtn) {
    togglePasswordBtn.addEventListener("click", () => {
        const passInput = $("loginPassword");
        if (!passInput) return;

        const isPassword = passInput.type === "password";
        passInput.type = isPassword ? "text" : "password";
        togglePasswordBtn.textContent = isPassword ? "🙈" : "👁";
        const label = isPassword ? "Hide password" : "Show password";
        togglePasswordBtn.setAttribute("aria-label", label);
        togglePasswordBtn.setAttribute("title", label);
    });
}

// Quick fill demo credentials button
const fillDemoBtn = $("fillDemoBtn");
if (fillDemoBtn) {
    fillDemoBtn.addEventListener("click", () => {
        const usernameInput = $("loginUsername");
        const passwordInput = $("loginPassword");

        if (usernameInput && passwordInput) {
            usernameInput.value = "admin";
            passwordInput.value = "1234";
            usernameInput.classList.remove("input-error");
            passwordInput.classList.remove("input-error");
            showLoginMessage("");

            const submitBtn = $("loginSubmitBtn");
            if (submitBtn) submitBtn.focus();
        }
    });
}

// Topbar & Sidebar Logout handlers (returns to login page)
const logoutBtn = $("logoutBtn");
if (logoutBtn) {
    logoutBtn.addEventListener("click", () => {
        setLoginState(false);
    });
}

const sidebarLogoutBtn = $("sidebarLogoutBtn");
if (sidebarLogoutBtn) {
    sidebarLogoutBtn.addEventListener("click", () => {
        setLoginState(false);
    });
}

// ==========================================================================
// Initial Application Startup
// ==========================================================================

// Ensure initial state shows the login page first when opened
renderAll();
setLoginState(false);
