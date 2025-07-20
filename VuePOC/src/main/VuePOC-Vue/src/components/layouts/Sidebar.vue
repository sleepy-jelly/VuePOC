<template>
    < <!-- Sidebar -->
        <aside id="sidebar" class="sidebar">
            <ul class="sidebar-nav" id="sidebar-nav">
                <li class="nav-item">
                <a class="nav-link" href="/dash-board">
                    <i class="bi bi-grid"></i>
                    <span>Dashboard</span>
                </a>
                </li>

                <li class="nav-item">
                <a class="nav-link collapsed" data-bs-target="#tables-nav" data-bs-toggle="collapse" href="#">
                    <i class="bi bi-layout-text-window-reverse"></i>
                    <span>sleep-jelly</span>
                    <i class="bi bi-chevron-down ms-auto"></i>
                </a>
                <ul id="tables-nav" class="nav-content collapse" data-bs-parent="#sidebar-nav">
                    <li>
                    <a href="/OpenAI/general-question">
                        <i class="bi bi-circle"></i>
                        <span>General Questions</span>
                    </a>
                    </li>
                    <li>
                    <a href="/OpenAI/audio-summary">
                        <i class="bi bi-circle"></i>
                        <span>Audio Summary</span>
                    </a>
                    </li>
                </ul>
                </li>

                <li class="nav-heading">Pages</li>
                <li class="nav-item">
                <a class="nav-link collapsed" href="#">
                    <i class="bi bi-person"></i>
                    <span>Profile</span>
                </a>
                </li>
                <li class="nav-item">
                <a class="nav-link collapsed" href="#">
                    <i class="bi bi-question-circle"></i>
                    <span>F.A.Q</span>
                </a>
                </li>
                <li class="nav-item">
                <a class="nav-link collapsed" href="/contact">
                    <i class="bi bi-envelope"></i>
                    <span>Contact</span>
                </a>
                </li>
            </ul>
        </aside>
</template>

<style scoped>
    @import '../../../src/index.css';
</style>
