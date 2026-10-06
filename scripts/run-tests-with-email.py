#!/usr/bin/env python3
import glob
import os
import smtplib
import ssl
import subprocess
import sys
import zipfile
from email.message import EmailMessage
from pathlib import Path
import xml.etree.ElementTree as ET


PROJECT_ROOT = Path(__file__).resolve().parent.parent
REPORTS_DIR = PROJECT_ROOT / "target" / "surefire-reports"
ARCHIVE_PATH = PROJECT_ROOT / "target" / "playwright-test-report.zip"
REQUIRED_SETTINGS = (
    "SMTP_SERVER",
    "SMTP_USERNAME",
    "SMTP_PASSWORD",
    "REPORT_EMAIL_TO",
    "REPORT_EMAIL_FROM",
)


def get_report_summary():
    totals = {"tests": 0, "failures": 0, "errors": 0, "skipped": 0}
    report_files = glob.glob(str(REPORTS_DIR / "TEST-*.xml"))
    for report_file in report_files:
        root = ET.parse(report_file).getroot()
        for key in totals:
            totals[key] += int(root.attrib.get(key, 0))

    if not report_files:
        return "No Surefire XML reports were generated.", report_files

    passed = totals["tests"] - totals["failures"] - totals["errors"] - totals["skipped"]
    summary = (
        f"Tests: {totals['tests']}, Passed: {passed}, "
        f"Failures: {totals['failures']}, Errors: {totals['errors']}, "
        f"Skipped: {totals['skipped']}"
    )
    return summary, report_files


def create_report_archive(report_files):
    ARCHIVE_PATH.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(ARCHIVE_PATH, "w", zipfile.ZIP_DEFLATED) as archive:
        for report_file in report_files:
            path = Path(report_file)
            archive.write(path, path.relative_to(PROJECT_ROOT))


def send_report(summary, test_status):
    server = os.environ["SMTP_SERVER"]
    port = int(os.getenv("SMTP_PORT", "587"))
    message = EmailMessage()
    message["Subject"] = f"Playwright test report: {test_status}"
    message["From"] = os.environ["REPORT_EMAIL_FROM"]
    message["To"] = os.environ["REPORT_EMAIL_TO"]
    message.set_content(
        f"Playwright UI test run: {test_status}\n\n"
        f"{summary}\n\n"
        f"Full Surefire reports are attached as {ARCHIVE_PATH.name}.\n"
    )
    message.add_attachment(
        ARCHIVE_PATH.read_bytes(),
        maintype="application",
        subtype="zip",
        filename=ARCHIVE_PATH.name,
    )

    context = ssl.create_default_context()
    if port == 465:
        with smtplib.SMTP_SSL(server, port, context=context) as smtp:
            smtp.login(os.environ["SMTP_USERNAME"], os.environ["SMTP_PASSWORD"])
            smtp.send_message(message)
    else:
        with smtplib.SMTP(server, port) as smtp:
            smtp.starttls(context=context)
            smtp.login(os.environ["SMTP_USERNAME"], os.environ["SMTP_PASSWORD"])
            smtp.send_message(message)


def main():
    missing = [name for name in REQUIRED_SETTINGS if not os.getenv(name)]
    if missing:
        print(
            "Set the following environment variables before running: "
            + ", ".join(missing),
            file=sys.stderr,
        )
        return 2

    try:
        test_result = subprocess.run(
            ["mvn", "--batch-mode", "clean", "test", "-Dplaywright.headless=true"],
            cwd=PROJECT_ROOT,
            check=False,
        )
    except FileNotFoundError:
        print("Maven (mvn) was not found. Install Maven and try again.", file=sys.stderr)
        return 127

    summary, report_files = get_report_summary()
    create_report_archive(report_files)
    test_status = "PASSED" if test_result.returncode == 0 else "FAILED"
    print(f"\n{test_status}: {summary}")
    print(f"Report archive: {ARCHIVE_PATH}")

    try:
        send_report(summary, test_status)
    except (OSError, ValueError, smtplib.SMTPException) as error:
        print(f"Could not send the test report email: {error}", file=sys.stderr)
        return test_result.returncode or 1

    print("Email report sent.")
    return test_result.returncode


if __name__ == "__main__":
    sys.exit(main())
